package com.example.compiler;

import com.example.annotation.DIActivity;
import com.example.annotation.DIView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by ZTH on 2018/1/19.
 */
@AutoService(Processor.class)
public class DIProcessor extends AbstractProcessor {
    private Elements elementUtils;
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // 规定需要处理的注解
        return Collections.singleton(DIActivity.class.getCanonicalName());
    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("DIProcessor");
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DIActivity.class);
        for (Element element : elements) {
            // 判断是否Class
            TypeElement typeElement = (TypeElement) element;
            List<? extends Element> members = elementUtils.getAllMembers(typeElement);
            MethodSpec.Builder bindViewMethodSpecBuilder = MethodSpec.methodBuilder("bindView")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.VOID)
                    .addParameter(ClassName.get(typeElement.asType()), "activity");
            for (Element item : members) {
                DIView diView = item.getAnnotation(DIView.class);
                if (diView == null){
                    continue;
                }
                bindViewMethodSpecBuilder.addStatement(String.format("activity.%s = (%s) activity.findViewById(%s)",item.getSimpleName(),ClassName.get(item.asType()).toString(),diView.value()));
            }
            TypeSpec typeSpec = TypeSpec.classBuilder("DI" + element.getSimpleName())
                    .superclass(TypeName.get(typeElement.asType()))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(bindViewMethodSpecBuilder.build())
                    .build();
            JavaFile javaFile = JavaFile.builder(getPackageName(typeElement), typeSpec).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }
}