package com.example.compiler;

import com.example.annotation.DIActivity;
import com.example.annotation.DIView;
import com.example.annotation.OnClick;
import com.example.annotation.Test;
import com.example.annotation.TestActivity;
import com.example.annotation.TestInt;
import com.example.annotation.TestString;
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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static javax.lang.model.type.TypeKind.DECLARED;
import static javax.lang.model.type.TypeKind.INT;

/**
 * Created by ZTH on 2018/1/19.
 */
@AutoService(Processor.class)
public class TestProcessor extends AbstractProcessor {
    private Elements elementUtils;
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // 规定需要处理的注解
        return Collections.singleton(TestActivity.class.getCanonicalName());
    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(TestActivity.class);
        for (Element element : elements) {
            // 判断是否Class
            TypeElement typeElement = (TypeElement) element;
            List<? extends Element> members = elementUtils.getAllMembers(typeElement);
            TestActivity activity = typeElement.getAnnotation(TestActivity.class);
            MethodSpec.Builder bindViewMethodSpecBuilder = MethodSpec.methodBuilder("setDefualt")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.VOID)
                    .addParameter(ClassName.get(typeElement.asType()), "activity",Modifier.FINAL);

            if(activity == null){
                continue;
            }
            bindViewMethodSpecBuilder.addStatement(String.format("activity.setContentView(%s)",activity.value()));
            for (Element item : members) {
                TestInt diView = item.getAnnotation(TestInt.class);
                OnClick onClick = item.getAnnotation(OnClick.class);
                TestString testString = item.getAnnotation(TestString.class);
                if (diView == null && onClick == null && testString == null){
                    continue;
                }

                if(diView != null){
                    if(item.asType().getKind() == INT)
                        bindViewMethodSpecBuilder.addStatement(String.format("activity.%s = %s",item.getSimpleName(),diView.value()));
                    if(item.asType().getKind() == DECLARED)
                        bindViewMethodSpecBuilder.addStatement(String.format("activity.%s = (%s)activity.findViewById(%s)",item.getSimpleName(),item.asType().toString(),diView.value()));

                }
                if(onClick != null){

                    String name = item.getSimpleName().toString();
                    bindViewMethodSpecBuilder.addStatement(String.format("activity.findViewById(%s).setOnClickListener(new android.view.View.OnClickListener() {\n" +
                            "            @Override\n" +
                            "            public void onClick(android.view.View v) {\n" +"activity.%s();"+
                            "                \n" +
                            "            }\n" +
                            "        });",onClick.value(),name));
                }

                if(testString != null){
                    bindViewMethodSpecBuilder.addStatement(String.format("activity.%s = \"%s\"",item.getSimpleName(),testString.value()));
                }



            }
            TypeSpec typeSpec = TypeSpec.classBuilder("Test" + element.getSimpleName())
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