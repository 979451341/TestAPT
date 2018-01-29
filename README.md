# TestAPT
APT使用注解代替int、String赋值，代替findViewById、setContentView、setOnClickListener命令

代替setContentView
@TestActivity(R.layout.activity_main)

代替findViewById
    @TestInt(R.id.btn)
    Button btn;
    
同时这个TestInt也可以给int类型变量赋值
@TestInt(10)
int a；

代替setOnClickListener
    @OnClick(R.id.btn)
    public void onClick(){

        tv.setText(str);
    }
    
String赋值
    @TestString("979451341")
    String str;

最后再oncreate里

TestXXXXActivity.setDefualt(this);
    例子代码如下：

@TestActivity(R.layout.activity_main)
public class MainActivity extends Activity{



    @TestInt(R.id.btn)
    Button btn;

    @OnClick(R.id.btn)
    public void onClick(){

        tv.setText(str);
    }


    @TestInt(R.id.tv)
    TextView tv;

    @TestString("979451341")
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TestMainActivity.setDefualt(this);


    }


}

想看如何实现，请看这六篇系列文章
http://blog.csdn.net/z979451341/article/details/79110118
