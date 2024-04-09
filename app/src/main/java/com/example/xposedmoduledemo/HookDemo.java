package com.example.xposedmoduledemo;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;


import com.alibaba.fastjson.JSON;


import com.koushikdutta.async.AsyncServerSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;



// 自定义的回调函数接口
public class HookDemo implements IXposedHookLoadPackage {

    static String strClassName = "HookDemo";
    Map<String, AsyncHttpServerResponse> vvv ;
    Map<String, AsyncHttpServerResponse> ttt;

    public static String generateRandomPhoneNumber() {
        Random random = new Random();
        StringBuilder phoneNumberBuilder = new StringBuilder("1"); // 手机号码以1开头

        // 随机生成后10位数字
        for (int i = 0; i < 10; i++) {
            int digit = random.nextInt(10); // 生成0到9之间的随机数字
            phoneNumberBuilder.append(digit);
        }

        return phoneNumberBuilder.toString();
    }

    private void getContact(LoadPackageParam lpparam, Object o) throws IOException {
        AsyncHttpServer  server = new AsyncHttpServer();
        XposedBridge.log("AsyncHttpServer listen ....");
        server.get("/get_contact", (request, response) -> {
            String wx_id =  request.getQuery().getString("wx_id");
            XposedBridge.log("get_contact wx_id:" + wx_id + ":" + o.hashCode());

            //先调用备注接口
            Class<?> easClass = XposedHelpers.findClass("com.tencent.mm.protocal.protobuf.eas",
                    lpparam.classLoader);

            Class<?> eojClass = XposedHelpers.findClass("com.tencent.mm.protocal.protobuf.eoj",
                    lpparam.classLoader);

            Class<?> eoiClass = XposedHelpers.findClass("com.tencent.mm.protocal.protobuf.eoi",
                    lpparam.classLoader);


            Class<?> kaClass = XposedHelpers.findClass("com.tencent.mm.plugin.messenger.foundation.a.a.k$a",
                    lpparam.classLoader);


            try {
                // 创建新实例
                Constructor<?> easConstructor = easClass.getDeclaredConstructor();
                easConstructor.setAccessible(true);
                Object eas = easConstructor.newInstance();

                Field amBm = easClass.getDeclaredField("amBm");
                amBm.setAccessible(true);
                amBm.set(eas, wx_id);

                Constructor<?> eoiConstructor = eoiClass.getDeclaredConstructor();
                eoiConstructor.setAccessible(true);
                Object eoi = eoiConstructor.newInstance();

                Field amOd = eoiClass.getDeclaredField("amOd");
                amOd.set(eoi, generateRandomPhoneNumber());

                List<Object> linkedList =  new LinkedList<>();
                linkedList.add(eoi);

                Constructor<?> eojConstructor = eojClass.getDeclaredConstructor();
                eojConstructor.setAccessible(true);
                Object eoj = eojConstructor.newInstance();

                Field zoO = eojClass.getDeclaredField("zoO");
                zoO.setAccessible(true);
                zoO.set(eoj, 1);

                Field amOe = eojClass.getDeclaredField("amOe");
                amOe.setAccessible(true);
                amOe.set(eoj, linkedList);

                Field amBa = easClass.getDeclaredField("amBa");
                amBa.setAccessible(true);
                amBa.set(eas, eoj);

                XposedBridge.log("proto build success:"+ JSON.toJSONString(eas));


                Constructor<?> constructor = kaClass.getDeclaredConstructor(int.class,
                        XposedHelpers.findClass("com.tencent.mm.cc.a",
                                lpparam.classLoader));
                constructor.setAccessible(true);
                Object ka = constructor.newInstance(60, eas);
                XposedBridge.log("ka newInstance:"+ ka.hashCode());

                Method getBuffer =  XposedHelpers.findMethodBestMatch(kaClass,"getBuffer");
                getBuffer.setAccessible(true);
                byte[] byteArray  = (byte[]) getBuffer.invoke(ka);
                String buffer = Arrays.toString(byteArray);
                XposedBridge.log("ka buffer:" + buffer);

                Class<?> lClass = XposedHelpers.findClass("com.tencent.mm.kernel.l", lpparam.classLoader);
                Object an =  XposedHelpers.callStaticMethod(lClass,
                        "an",
                        XposedHelpers.findClass("com.tencent.mm.plugin.messenger.foundation.a.o",
                                lpparam.classLoader)
                );

                XposedBridge.log("an callllllllll:"+ an.hashCode()+ ":" + an.getClass().getName());

                Object bJi =  XposedHelpers.callMethod(an, "bJi");

                XposedBridge.log("Bji call result :" + bJi.hashCode()+ ":"+ bJi.getClass().getName());

                try {
                    // 通过反射调用父类的方法
                    Method dMethod =  XposedHelpers.findMethodBestMatch(XposedHelpers.findClass("com.tencent.mm.bh.o", lpparam.classLoader),
                            "d",
                            XposedHelpers.findClass("com.tencent.mm.plugin.messenger.foundation.a.a.k$b", lpparam.classLoader));

                    dMethod.invoke(bJi, ka);
                    XposedBridge.log("oplog call success");
                } catch (InvocationTargetException e) {
                    // 捕获 InvocationTargetException
                    Throwable originalException = e.getTargetException();
                    if (originalException != null) {
                        // 获取被包装的原始异常
                        XposedBridge.log("oplog call exception:"+ originalException.toString());
                    }
                }

            }  catch (Exception e) {
                XposedBridge.log("catch Exception:" + e.toString());
                throw new RuntimeException(e);
            }

            XposedBridge.log("start get contact");

            SparseArray<List<String>> sparseArray = new SparseArray<>();
            List<String> linkedList = new LinkedList<>();
            linkedList.add(wx_id);
            sparseArray.put(1, linkedList);
            Method c =  XposedHelpers.findMethodBestMatch(o.getClass(),"c", SparseArray.class);
            c.setAccessible(true);
            Method bRH =  XposedHelpers.findMethodBestMatch(o.getClass(),"bRH");
            bRH.setAccessible(true);
            try {
                c.invoke(o, sparseArray);
                bRH.invoke(o);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            ttt.put(wx_id, response);
        });
        server.listen(8989);
    }


    // 递归查找ListView
    private ListView findListView(View view) {
        if (view instanceof ListView) {
            return (ListView) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childView = viewGroup.getChildAt(i);
                ListView listView = findListView(childView);
                if (listView != null) {
                    return listView;
                }
            }
        }
        return null;
    }

    private void enumerateViews(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childView = viewGroup.getChildAt(i);
                // 这里可以获取子视图的信息，比如ID、标签等
                int id = childView.getId();
                String tag = (String) childView.getTag();
                // 处理子视图的信息，比如打印出来
                if (id != View.NO_ID) {
                    // 如果子视图有ID，则打印ID
                    // 这里可以根据实际需求进行操作，比如保存到列表中等
                    XposedBridge.log("View ID: " + id);
                }
                if (tag != null) {
                    // 如果子视图有标签，则打印标签
                    // 这里可以根据实际需求进行操作，比如保存到列表中等
                    XposedBridge.log("View Tag: " + tag);
                }
                // 递归遍历子视图的子视图
                enumerateViews(childView);
            }
        }
    }

    private AdapterView.OnItemClickListener getOriginalItemClickListener(ListView listView) {
        try {
            // 获取mOnItemClickListener字段
            Field field = ListView.class.getDeclaredField("mOnItemClickListener");
            field.setAccessible(true);
            // 获取原始的OnItemClickListener
            return (AdapterView.OnItemClickListener) field.get(listView);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            XposedBridge.log(e.toString());
        }
        return null;
    }

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {

        // 被Hook操作的目标Android应用的包名，进行Hook操作的过滤
        String strPackageName = "com.tencent.mm";
        if (lpparam.packageName.startsWith(strPackageName)) {
            XposedBridge.log("Loaded App:" + lpparam.packageName);

            final Class<?> clazz = XposedHelpers.findClassIfExists("com.tencent.mm.ui.chatting.component.aj$12", lpparam.classLoader);
            if (clazz == null) {
                XposedBridge.log("aj$12 class not find ");
                return;
            }

            final Class<?> clazz2 = XposedHelpers.findClassIfExists("com.tencent.mm.ui.chatting.component.aj$13", lpparam.classLoader);
            if (clazz == null) {
                XposedBridge.log("aj.13 class not find ");
                return;
            }

            final Class<?> clazz3 = XposedHelpers.findClassIfExists("com.tencent.mm.ui.chatting.component.aj.2", lpparam.classLoader);
            if (clazz == null) {
                XposedBridge.log("aj.13 class not find ");
                return;
            }

            XposedBridge.log("aj " + clazz.getName() + ":" + clazz2.getName());


            findAndHookMethod(View.class, "setOnClickListener", View.OnClickListener.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    View view = (View) param.thisObject;
                    String Str = null;
                    if (view instanceof TextView) {
                        Str = ((TextView) view).getText().toString();
                    }
                    int btnId = view.getId();
                    if (btnId == 2131313841 && param.args[0] != null) {
                        View.OnClickListener listener = (View.OnClickListener) param.args[0];
                        XposedBridge.log("listener:" + listener.getClass().toString());

                        // 创建一个新的 OnClickListener 对象
                        View.OnClickListener hookedListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 在点击事件前执行自定义操作
                                XposedBridge.log("hook listener:" + v.getId());
                                // 如果需要，您可以在此处调用原始 OnClickListener 的 onClick 方法
                                if (listener != null) {
                                    listener.onClick(v);
                                }
                            }
                        };

                        // 用新的 OnClickListener 对象替换传递给 setOnClickListener 方法的参数
                        param.args[0] = hookedListener;

                    }
                    //      XposedBridge.log(Str + ";" + "Id:" + btnId);
                }


                /*
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    View view = (View) param.thisObject;
                    View.OnClickListener listener = (View.OnClickListener) param.args[0];
                    // Check if the view is a TextView and has the desired ID
                    if ((int) view.getId() == 2131313841 && listener != null) {
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Perform your custom action here
                                XposedBridge.log("hook listener:" + v.getId());
                                listener.onClick(v);
                            }
                        });
                    }
                }
                */
            });


            // Hook 静态函数
            XposedHelpers.findAndHookMethod("com.tencent.mm.pluginsdk.ui.span.s", lpparam.classLoader,
                    "a", Activity.class, String.class, int.class ,  Bundle.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    // 在调用静态函数前执行的逻辑
                    XposedBridge.log("com.tencent.mm.pluginsdk.ui.span.s call -------------");
                    Bundle bundle = (Bundle)param.args[3];
                    String str = (String)param.args[1];
                    String Contact_User  = bundle.getString("Contact_User");
                    String Contact_Mobile_MD5 = bundle.getString("Contact_Mobile_MD5");
                    XposedBridge.log("contact:" + Contact_User + ":" + " str:" +  str + " md5:" + Contact_Mobile_MD5);
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    // 在调用静态函数后执行的逻辑
                }
            });

            XposedHelpers.findAndHookMethod("com.tencent.mm.storage.bd", lpparam.classLoader,
                    "TC", String.class,  new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            /*
                            Object returnValue = param.getResult();
                            String str = (String) param.args[0];
                            if (returnValue != null) {
                                String lBh = (String) XposedHelpers.getObjectField(returnValue, "lBh");
                                XposedBridge.log("storage.bd lBh:" + lBh);
                            } */
                        }
                    });


            // Hook Dialog 类的 show 方法
            XposedHelpers.findAndHookMethod("android.app.Dialog", lpparam.classLoader, "show", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    // 在调用 Dialog.show() 前执行的逻辑
                    XposedBridge.log("Dialog is about to be shown!");
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    // 在调用 Dialog.show() 后执行的逻辑
                    XposedBridge.log("Dialog has been shown!");
                    Dialog dialog = (Dialog) param.thisObject;
                    ListView listView = findListView(dialog.getWindow().getDecorView());
                    if (listView != null) {
                        AdapterView.OnItemClickListener originalListener =  listView.getOnItemClickListener();
                        XposedBridge.log("Dialog find this listView :" + listView.getId() + ":" + originalListener.getClass().getName());
                        // 找到ListView后，设置它的点击事件
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // 处理ListView的点击事件
                                XposedBridge.log("onItemClick click: " + id + ":" + view.getId());
                                originalListener.onItemClick(parent, view, position, id);
                            }
                        });
                    }
                }
            });



            XposedHelpers.findAndHookMethod("com.tencent.mm.storage.bd", lpparam.classLoader,
                    "az",
                    XposedHelpers.findClassIfExists("com.tencent.mm.storage.ax", lpparam.classLoader),
                    new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("-------storage.ax result ----:" + JSON.toJSONString((param.args[0])));
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });


            XposedHelpers.findAndHookMethod("com.tencent.mm.contact.a",
                    lpparam.classLoader, "a",
                    XposedHelpers.findClassIfExists("com.tencent.mm.storage.ax", lpparam.classLoader),
                    java.lang.String.class,
                    ArrayList.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("-------contact.ax result ----:" + JSON.toJSONString((param.args[0])));
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });


            /*

            XposedHelpers.findAndHookMethod("com.tencent.wcdb.database.SQLiteConnection", lpparam.classLoader,
                    "executeForChangedRowCount",
                    java.lang.String.class,
                    java.lang.Object[].class,
                    XposedHelpers.findClassIfExists("com.tencent.wcdb.support.CancellationSignal", lpparam.classLoader),
                    new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    String sql = (String) param.args[0];
                    if( (sql.contains("UPDATE") || sql.contains("update")) && sql.contains("rcontact") ) {
                        XposedBridge.log("sql:" + sql + ":" + JSON.toJSONString(param.args[1]));
                        XposedBridge.log("Dump Stack: ---------------start----------------");
                        Throwable ex = new Throwable();
                        StackTraceElement[] stackElements = ex.getStackTrace();
                        if (stackElements != null) {
                            for (int i = 0; i < stackElements.length; i++) {
                                XposedBridge.log("Dump Stack :"+i+":" +  stackElements[i].getClassName()
                                        +"----"+stackElements[i].getFileName()
                                        +"----" + stackElements[i].getLineNumber()
                                        +"----" +stackElements[i].getMethodName());
                            }
                        }
                        XposedBridge.log("Dump Stack: ---------------over----------------");
                    }
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });

             */

/*
            XposedHelpers.findAndHookMethod("com.tencent.mm.bh.o", lpparam.classLoader,
                    "a",
                    XposedHelpers.findClassIfExists("com.tencent.mm.bh.o.class", lpparam.classLoader),
                    new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("bh.o timer event call----------");
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });



            XposedHelpers.findAndHookMethod("com.tencent.mm.bh.o", lpparam.classLoader, "d",
                    XposedHelpers.findClass("com.tencent.mm.plugin.messenger.foundation.a.a.k$b", lpparam.classLoader),
                    new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("bh.o call 122222222222");
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });


            XposedHelpers.findAndHookMethod("com.tencent.mm.av.u",
                    lpparam.classLoader, "a",
                    XposedHelpers.findClass("com.tencent.mm.av.u", lpparam.classLoader),
                    XposedHelpers.findClass("com.tencent.mm.av.r",lpparam.classLoader),
                    new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object u = param.args[0];
                    Object r = param.args[1];
                    XposedBridge.log("do request u:"+ u.getClass().getName() +":"+ JSON.toJSONString(u));
                    XposedBridge.log("do request v:"+ r.getClass().getName() +":"+ JSON.toJSONString(r));
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });


            XposedHelpers.findAndHookMethod("com.tencent.mm.av.r",
                    lpparam.classLoader, "dispatch",
                    XposedHelpers.findClass(" com.tencent.mm.network.g", lpparam.classLoader),
                    XposedHelpers.findClass("com.tencent.mm.network.t", lpparam.classLoader),
                    XposedHelpers.findClass("com.tencent.mm.network.n", lpparam.classLoader),
                    new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object ng = param.args[0];
                    Object nt = param.args[1];
                    Object nn = param.args[2];
                    XposedBridge.log("network ng:" + ng.getClass().getName() + ":" + JSON.toJSONString(ng));
                    XposedBridge.log("network nt:" + nt.getClass().getName() + ":" + JSON.toJSONString(nt));
                    XposedBridge.log("network nn:" +  nn.getClass().getName() + ":"+ JSON.toJSONString(nn));

                    Object rFp = XposedHelpers.getObjectField(ng,"rFp");
                    XposedBridge.log("network rFp:" +  rFp.getClass().getName() + ":"+ JSON.toJSONString(rFp));
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });

            XposedHelpers.findAndHookMethod("com.tencent.mm.network.h$a$a",
                    lpparam.classLoader, "a",
                    XposedHelpers.findClass("com.tencent.mm.network.u", lpparam.classLoader),
                    XposedHelpers.findClass(" com.tencent.mm.network.o", lpparam.classLoader),
                    new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object o = param.thisObject;
                    XposedBridge.log("h$a$a --------: " +JSON.toJSONString(o) );
                    Object syK = XposedHelpers.getObjectField(
                           o,
                            "mRemote"
                    );
                    if (syK != null) {
                        XposedBridge.log("network syK -----xxxxx------:" +  syK.getClass().getName() + ":"+ JSON.toJSONString(syK));
                    }

                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });
            */

            XposedHelpers.findAndHookMethod("com.tencent.mm.network.af",
                    lpparam.classLoader,
                    "a",
                    XposedHelpers.findClass("com.tencent.mm.network.u",lpparam.classLoader),
                    XposedHelpers.findClass("com.tencent.mm.network.o",lpparam.classLoader),
                    XposedHelpers.findClass("com.tencent.mm.network.e",lpparam.classLoader),
                    int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object u = param.args[0];
                    String uri = (String) XposedHelpers.callMethod(u,"getUri");
                    int type = (int) XposedHelpers.callMethod(u, "getType");
                    int  options = (int)XposedHelpers.callMethod(u, "getOptions");
                    XposedBridge.log("http uri:=" + uri+ ":"+ type + ":" + options);
                    Object bot =  XposedHelpers.callMethod(u, "bOT");
                    Object bou =  XposedHelpers.callMethod(u, "bOU");
                    if (bot != null) {
                        XposedBridge.log("uri bot :" + bot.getClass().toString());
                    }
                    if (bot != null) {
                        XposedBridge.log("uri bou :" + bou.getClass().toString());
                    }
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });

            /*


            XposedHelpers.findAndHookMethod("com.tencent.mm.av.ab",
                    lpparam.classLoader, "a",
                    XposedHelpers.findClassIfExists("com.tencent.mm.av.c",lpparam.classLoader),
                    XposedHelpers.findClassIfExists(" com.tencent.mm.av.ab$a",lpparam.classLoader),
                    boolean.class,
                    new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object c = param.args[0];
                    int funcId = (int)XposedHelpers.getObjectField(c, "funcId");
                    if (funcId == 182) {
                        Object rEn = XposedHelpers.getObjectField(c, "rEn");
                        if (rEn != null) {
                            Object rEt = XposedHelpers.getObjectField(rEn, "rEt");
                            XposedBridge.log("request :" + rEt.getClass().getName() + ":" + JSON.toJSONString(rEt));
                        }
                        Object rEo = XposedHelpers.getObjectField(c, "rEo");
                        if (rEo != null) {
                            Object rEt = XposedHelpers.getObjectField(rEn, "rEt");
                            XposedBridge.log("response :" + rEt.getClass().getName() + ":" + JSON.toJSONString(rEt));

                        }
                    }

                }
            }); */

            XposedHelpers.findAndHookMethod("com.tencent.mm.bb.c$6",
                    lpparam.classLoader,
                    "callback",
                    int.class,
                    int.class,
                    java.lang.String.class,
                    XposedHelpers.findClassIfExists(" com.tencent.mm.av.c", lpparam.classLoader),
                    XposedHelpers.findClassIfExists("com.tencent.mm.av.r", lpparam.classLoader),
                    new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object r = param.args[4];
                    if (r != null) {
                        int getType = (int)XposedHelpers.callMethod(r,"getType");
                        if (getType == 182) {
                            Object c = param.args[3];
                            Object rEo =  XposedHelpers.getObjectField(c, "rEo");
                            if (rEo != null) {
                                Object rEt = XposedHelpers.getObjectField(rEo, "rEt");
                               // XposedBridge.log("response rEt :" + rEt.getClass().getName()+ ":"+JSON.toJSONString(rEt));
                                //alUX
                                Object alUX = XposedHelpers.getObjectField(rEt,"alUX");
                                if (alUX != null) {
                                    LinkedList<Object> linkedList = (LinkedList<Object>) alUX;
                                    linkedList.forEach(e -> {
                                        Object akMp = XposedHelpers.getObjectField(e, "akMp");
                                        String ancD = "";
                                        if (akMp != null) {
                                            ancD = (String) XposedHelpers.getObjectField(akMp, "ancD");
                                        }
                                        String tnI = (String) XposedHelpers.getObjectField(e, "tnI");
                                        XposedBridge.log("got the contact:"+ ancD + ":" + tnI);
                                        AsyncHttpServerResponse response = ttt.get(ancD);
                                        if (response != null) {
                                            response.send("got the contact:"+ ancD + ":" + tnI);
                                            ttt.remove(ancD);
                                        }
                                    });
                                }
                            }

                        }
                    }
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });


            XposedHelpers.findAndHookConstructor("com.tencent.mm.bb.c",
                    lpparam.classLoader,
                    new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("bbb ccc ----------- constructor---------------:" +  param.thisObject.hashCode());
                    vvv = new ConcurrentHashMap<>();
                    ttt = new ConcurrentHashMap<>();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getContact(lpparam, param.thisObject);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }}).start();
                }
            });

            XposedHelpers.findAndHookMethod("com.tencent.mm.bb.c",
                    lpparam.classLoader, "c",
                    android.util.SparseArray.class,
                    new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    SparseArray<List<String>> sparseArray = (SparseArray<List<String>>) param.args[0];
                    for (int i2 = 0; i2 < sparseArray.size(); i2++) {
                        int keyAt = sparseArray.keyAt(i2);
                        for (String str : sparseArray.valueAt(i2)) {
                           XposedBridge.log("SparseArray:"+ keyAt+":"+ str);
                        }
                    }
                    XposedBridge.log("SparseArray :" + JSON.toJSONString(param.args[0])+ ":" + param.thisObject.hashCode());
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });


            XposedHelpers.findAndHookMethod("com.tencent.mm.bb.c",
                    lpparam.classLoader, "bRH", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("bRH CALL --------------");
                    /*
                    for (Map.Entry<String, AsyncHttpServerResponse> entry : vvv.entrySet()) {
                                    SparseArray<List<String>> sparseArray = new SparseArray<>();
                        List<String> linkedList = new LinkedList<>();
                        linkedList.add(entry.getKey());
                        sparseArray.put(1, linkedList);

                        Method privateMethod =  XposedHelpers.findMethodBestMatch(param.thisObject.getClass(),"c", SparseArray.class);
                        privateMethod.setAccessible(true);
                        try {
                            privateMethod.invoke(param.thisObject, sparseArray);
                            ttt.put(entry.getKey(),entry.getValue());
                            vvv.remove(entry.getKey());
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }*/
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });


            XposedHelpers.findAndHookConstructor("com.tencent.mm.bh.b",
                    lpparam.classLoader,
                    java.util.List.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("BHhhhhhhh:"+ JSON.toJSONString(param.args[0]));
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });


            /*
            XposedHelpers.findAndHookMethod("com.tencent.mm.bh.o",
                    lpparam.classLoader,
                    "d", com.tencent.mm.plugin.messenger.foundation.a.a.k$b.class,
                    new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });

             */

            XposedHelpers.findAndHookMethod("com.tencent.mm.pluginsdk.ui.span.s",
                    lpparam.classLoader, "a",
                    android.app.Activity.class,
                    XposedHelpers.findClassIfExists("com.tencent.mm.storage.ax", lpparam.classLoader),
                    java.lang.String.class,
                    java.util.ArrayList.class
                    , new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    String str = (String)param.args[2];
                    XposedBridge.log("span.s ---:"+  str + ":" + JSON.toJSONString(param.args[1]));
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });


        }


    }
}

