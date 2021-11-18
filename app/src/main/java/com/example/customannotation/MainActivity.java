package com.example.customannotation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.ioc_annotation_lib.InjectTool;
import com.example.ioc_annotation_lib.annation.BindView;
import com.example.ioc_annotation_lib.annation.ContentView;
import com.example.ioc_annotation_lib.annation.OnClick;
import com.example.ioc_annotation_lib.annation_common.OnClickCommon;
import com.example.ioc_annotation_lib.annation_common.OnClickLongCommon;

@ContentView(R.layout.activity_main) // ContentView注解 ==  setContentView
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bt_test1)  // 模仿 XUtils
    private Button button1;  // Dagger2 为什么不能写private

    @BindView(R.id.bt_test2) // 模仿 XUtils
    private Button button2;

    @BindView(R.id.bt_test3) // 模仿 XUtils
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        // 注解tool进行绑定
        InjectTool.inject(this);

        Toast.makeText(this, button1.getText(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, button2.getText(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, button3.getText(), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.bt_test3) // 模仿 XUtils
    private void show() {
        Toast.makeText(this, "show is run", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.bt_test4) // 模仿 XUtils
    private void show2() {
        Toast.makeText(this, "show2 is run", Toast.LENGTH_SHORT).show();
    }

    @OnClickCommon(R.id.bt_t1) // 点击事件
    private void test111() {
        Toast.makeText(this, "兼容事件 点击", Toast.LENGTH_SHORT).show();
    }

    @OnClickLongCommon(R.id.bt_t2) // 长按事件
    private boolean test222() {
        Toast.makeText(this, "兼容事件 长按", Toast.LENGTH_SHORT).show();
        return false;
    }
}