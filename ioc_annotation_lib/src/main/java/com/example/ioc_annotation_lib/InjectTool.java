package com.example.ioc_annotation_lib;

import android.util.Log;
import android.view.View;

import com.example.ioc_annotation_lib.annation.BindView;
import com.example.ioc_annotation_lib.annation.ContentView;
import com.example.ioc_annotation_lib.annation.OnClick;
import com.example.ioc_annotation_lib.annation_common.OnBaseCommon;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectTool {
    private static final String TAG = InjectTool.class.getSimpleName();

    public static void inject(Object object) {
        injectSetContentView(object); // 注射 SetContentView

        injectBindView(object);

        injectClick(object); // 只处理点击事件

        // >>>>>>>>>>>>>>>>>>>>> 下面是 兼容版本的事件代码
        injectEvent(object);
    }


    /**
     * 把布局绑定到 Activity中去
     *
     * @param object MainActivity 的 this
     */
    private static void injectSetContentView(Object object) {
        // 反射获取到注解
        Class<?> aClass = object.getClass();
        // 获取@ContentView注解
        ContentView annotation = aClass.getAnnotation(ContentView.class);
        // 拿到注解的value 也就是对应的布局id
        if (null == annotation) {
            Log.d(TAG, "ContentView is null ");
            return;
        }
        // 获取 R.layout.activity_main
        int layoutId = annotation.value();
        // 反射执行 setContentView(layoutID == R.layout.activity_main);
        try {
            Method setContentViewMethod = aClass.getMethod("setContentView", int.class);
            // 通过 Method 执行 setContentView 函数
            setContentViewMethod.invoke(object, layoutId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把布局控件到 Activity中去
     *
     * @param object MainActivity 的 this
     */
    private static void injectBindView(Object object) {
        Class<?> aClass = object.getClass();
        // 通过反射获取所有的属性
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) { // 类似于 控件 button1 button2 button3
            field.setAccessible(true);
            // 只关心 BindVIew注解的 字段，其他的不管
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView == null) {
                Log.d(TAG, "BindView is null");
                continue; // 结束本次，继续下一次
            }
            // 代表一定有 BindView 注解的
            // 获取 R.id.bt_t2 == viewID
            int value = bindView.value();
            // 1.反射设置findViewById
            try {
                Method findViewByIdMethod = aClass.getMethod("findViewById", int.class);
                Object view = findViewByIdMethod.invoke(object, value);
                // 2 获取反射的值 赋值给 button1  button2  button3
                field.set(object, view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void injectClick(Object object) {
        Class<?> aClass = object.getClass();
        // 获取cls中所有的方法
        Method[] methods = aClass.getDeclaredMethods();
        // 过滤 带OnClick注解的方法
        for (Method method : methods) {
            method.setAccessible(true);
            // 获取onClick注解
            OnClick annotation = method.getAnnotation(OnClick.class);
            if (annotation == null) {
                Log.d(TAG, "BindView is null");
                continue; // 结束本次，继续下一次
            }
            // R.id.bt_test3 == viewID
            int viewID = annotation.value();
            try {
                // 1.findViewById 反射寻找 findViewById
                Method findViewByIdMethod = aClass.getMethod("findViewById", int.class);
                // 2.findViewById 反射执行 的 成果拿到   resultView == button1的实例对象了
                Object resultView = findViewByIdMethod.invoke(object, viewID);
                // 3.findViewById 反射执行 的 成果拿到 .setOnClickListener { }
                View view = (View) resultView;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 4.反射回调到 Click注解的show函数  最终一定要反射执行 show函数
                        // method == 第三个函数 show函数，为什么，因为前面代码已经过滤了
                        try {
                            method.invoke(object);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下面是 兼容版本的事件代码
     * 把布局事件到 Activity中去
     *
     * @param object MainActivity 的 this
     */
    private static void injectEvent(Object object) {
        Class<?> mMainActivityClass = object.getClass();
        // 获取所有方法
        Method[] declaredMethods = mMainActivityClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            declaredMethod.setAccessible(true);
            // 遍历方法上  所有的注解集
            Annotation[] annotations = declaredMethod.getAnnotations();
            for (Annotation annotation : annotations) {
                // 获取当前注解的父注解 是否有 OnBaseCommon
                Class<? extends Annotation> annotationType = annotation.annotationType();
                OnBaseCommon onBaseCommon = annotationType.getAnnotation(OnBaseCommon.class);
                if (null == onBaseCommon) {
                    Log.d(TAG, "OnBaseCommon is null");
                    continue; // 结束本次，继续下一次
                }
                // 终于找到了 此注解包含 OnBaseCommon
                // 获取事件三要数
                String setCommonListener = onBaseCommon.setCommonListener();
                Class setCommonObjectListener = onBaseCommon.setCommonObjectListener();
                String callbackMethod = onBaseCommon.callbackMethod(); // 感觉3在，多个方法下才有用
                // 动态代理完成
                try {
                    // 由于上面无法明确 子注解   annotationType获取到子注解
                    // 获取 @OnClickCommon(R.id.bt_t1) value == R.id.bt_t1
                    Method valueMethod = annotationType.getDeclaredMethod("value");
                    valueMethod.setAccessible(true);
                    int value = (int) valueMethod.invoke(annotation);

                    // 就是执行 findViewById 拿到 button1 == resultView
                    Method findViewByIdMethod = mMainActivityClass.getMethod("findViewById", int.class);
                    Object resultView = findViewByIdMethod.invoke(object, value);
                    /*View button1 = (View) resultView;
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });*/
                    // button1.setOnClickListener(new OnClickList)

                    // 再次启用反射  button1 class    setOnClickListener（事件三要数之一）   setCommonObjectListener（事件三要数之一）
                    Method mViewMethod = resultView.getClass().getMethod(setCommonListener, setCommonObjectListener);

                    // 动态代理 监听 第三个要素
                    Object proxy = Proxy.newProxyInstance(
                            setCommonObjectListener.getClassLoader(), // 动态代理内部需要的 getClassLoader
                            new Class[]{setCommonObjectListener}, // 动态代理只监听 第二要素 的 接口
                            new InvocationHandler() {
                                @Override
                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                    // 代理里边可以区分方法名字的
                                    return declaredMethod.invoke(object, null);
                                }
                            }
                    );
                    // 事件三要素的 第三个 最终的事件消费
                    mViewMethod.invoke(resultView, proxy);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
