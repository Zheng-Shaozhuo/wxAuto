package com.example.zheng_shaozhuo.accessibility_20180115;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by Zheng-Shaozhuo on 2018/1/15.
 */

public class MyAccessibility extends AccessibilityService {
    private final String TAG = "vgambler";
    private int normalStep = 99;
    private int addedNum = 0;
    private int isFreqperat = 0;
    private String[] wxList = {};

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        new Thread(new Runnable() {
            @Override
            public void run() {
                wxList = getListByHttp();
                Log.i(TAG, "wxList length is " + wxList.length);
            }
        }).start();

//        AccessibilityServiceInfo mServeiceInfo = new AccessibilityServiceInfo();
//        mServeiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
//        mServeiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
//        setServiceInfo(mServeiceInfo);

    }

    private String[] getListByHttp() {
        String[] targetList = {};
        String result = null;
        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader in = null;
        try {
            url = new URL("http://iface.vgambler.xyz/");
            connection = (HttpURLConnection) url.openConnection();
            in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer strBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }

            result = strBuffer.toString();
            Log.i(TAG, result);
            targetList = result.split(",");
        } catch(Exception e) {
            Log.i(TAG, e.toString());
        }

        return targetList;
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.i(TAG, "onKeyEvent : " + event.getCharacters());
        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();
        String eventTypeName = "";
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventTypeName = "TYPE_VIEW_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                eventTypeName = "TYPE_VIEW_FOCUSED";
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                eventTypeName = "TYPE_VIEW_LONG_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                eventTypeName = "TYPE_VIEW_SELECTED";
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                eventTypeName = "TYPE_VIEW_TEXT_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventTypeName = "TYPE_WINDOW_STATE_CHANGED";
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                eventTypeName = "TYPE_NOTIFICATION_STATE_CHANGED";
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                eventTypeName = "TYPE_TOUCH_EXPLORATION_GESTURE_END";
                break;
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                eventTypeName = "TYPE_ANNOUNCEMENT";
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                eventTypeName = "TYPE_TOUCH_EXPLORATION_GESTURE_START";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                eventTypeName = "TYPE_VIEW_HOVER_ENTER";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                eventTypeName = "TYPE_VIEW_HOVER_EXIT";
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                eventTypeName = "TYPE_VIEW_SCROLLED";
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                eventTypeName = "TYPE_VIEW_TEXT_SELECTION_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                eventTypeName = "TYPE_WINDOW_CONTENT_CHANGED";
                break;
        }

        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && accessibilityEvent.getClassName().equals("com.tencent.mm.plugin.search.ui.FTSAddFriendUI")) {
            normalStep = 0;
        }
        if (isFreqperat == 3) {
            addedNum++;
            isFreqperat = 0;
        }

        if (addedNum < wxList.length && wxList.length > 0) {
            if (0 == normalStep) {
                try {
                    sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (EditSearchV()) {
                    normalStep = 1;

                    try {
                        sleep((long) (Math.random() * 2800 + 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    normalStep = 5;
                }
            } else if (1 == normalStep) {
                if (clickSearch()) {
                    normalStep = 2;

                    try {
                        sleep((long) (Math.random() * 2800 + 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (2 == normalStep) {
                if (clickAdd()) {
                    normalStep = 3;
                    addedNum++;

                    try {
                        sleep((long) (Math.random() * 2800 + 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "添加到通讯录 点击...");
                } else if (checkWordsBytxt(getRootInActiveWindow(), "设置备注和标签") || justFindNode(getRootInActiveWindow(), "设置备注和标签", null, "android.widget.TextView")) {
                    if (clickAdd()) {
                        normalStep = 3;
                        addedNum++;
                    } else {
                        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        normalStep = 5;
                        addedNum++;

                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "该联系人已存在");
                    }
                } else if (checkWordsBytxt(getRootInActiveWindow(), "该用户不存在") || justFindNode(getRootInActiveWindow(), "该用户不存在", null, "android.widget.TextView")) {
                    while (true) {
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (clickClean()) {
                            Log.i(TAG, "该用户不存在，那么add : " + addedNum);
                            normalStep = 0;
                            addedNum++;
                            break;
                        }
                    }
                } else if (checkWordsBytxt(getRootInActiveWindow(), "被搜帐号状态异常，无法显示") || justFindNode(getRootInActiveWindow(), "被搜帐号状态异常，无法显示", null, "android.widget.TextView")) {
                    while (true) {
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (clickClean()) {
                            Log.i(TAG, "该用户不存在，那么add : " + addedNum);
                            normalStep = 0;
                            addedNum++;
                            break;
                        }
                    }
                } else if (checkWordsBytxt(getRootInActiveWindow(), "操作过于频繁，请稍后再试") || justFindNode(getRootInActiveWindow(), "操作过于频繁，请稍后再试", null, "android.widget.TextView")) {
                    while (true) {
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (clickClean()) {
                            normalStep = 0;
                            addedNum++;
                            break;
                        }
                    }
                    isFreqperat++;
                } else if (findNode2Execute(getRootInActiveWindow(), wxList[addedNum].toLowerCase(), null, "android.widget.TextView", null)) {
                    normalStep = 2;
                    addedNum++;

                    try {
                        sleep((long) (Math.random() * 2800 + 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "不是添加到通讯录 点击...");
                }

            } else if (3 == normalStep) {
                if (clickSend()) {
                    normalStep = 4;

                    try {
                        sleep((long) (Math.random() * 11200 + 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (4 == normalStep) {
                clickBack();
                normalStep = 5;

                try {
                    sleep((long) (Math.random() * 2800 + 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if (5 == normalStep) {
                if (clickClean()) {
                    normalStep = 0;

                    try {
                        sleep((long) (Math.random() * 2800 + 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                normalStep = 99;
            }
        } else {
            Log.i(TAG, wxList.length + ", 人加完了，加完了，加完了。。。。。。。。。。。。。。");
        }

        bl(accessibilityEvent.getSource());
        Log.i(TAG, "Step: " + normalStep + ", eventTypeName:" + eventTypeName + ", Version: " + android.os.Build.VERSION.SDK_INT + ", " + accessibilityEvent.getClassName() + ", " + accessibilityEvent.getPackageName());
    }

    private boolean EditSearchV() {
        Log.i(TAG, "target node text: 搜索, contentDescription: , className: android.widget.EditText, inputText: " + wxList[addedNum]);

        return findNode2Execute(getRootInActiveWindow(), "搜索", null, "android.widget.EditText", wxList[addedNum]);
    }

    private boolean clickSearch() {
        Log.i(TAG, "target node text: 搜索, contentDescription: , className: android.widget.TextView");

        return findNode2Execute(getRootInActiveWindow(), "搜索:", null, "android.widget.TextView", null);
    }

    private boolean clickClean() {
        Log.i(TAG, "target node text: 清除, contentDescription: , className: android.widget.ImageButton");

        return findNode2Execute(getRootInActiveWindow(), "清除", null, "android.widget.ImageButton", null);
    }

    private boolean clickAdd() {
        Log.i(TAG, "target node text: 添加到通讯录, contentDescription: , className: android.widget.Button");

        return findNode2Execute(getRootInActiveWindow(), "添加到通讯录", null, "android.widget.Button", null);
    }

    private boolean clickSend() {
        Log.i(TAG, "target node text: 发送, contentDescription: , className: android.widget.TextView");

        return findNode2Execute(getRootInActiveWindow(), "发送", null, "android.widget.TextView", null);
    }

    private void clickBack() {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }


    private void bl(AccessibilityNodeInfo node) {
        if (null != node) {
            int num = node.getChildCount();
            if (num > 0) {
                for (int i = 0; i < num; i++) {
                    bl(node.getChild(i));
                }
            } else {
                Log.i(TAG, "current node text: " + node.getText() + ", contentDescription: " + node.getContentDescription() + ", className: " + node.getClassName());
            }
        }
    }

    private boolean checkWordsBytxt(AccessibilityNodeInfo root, String kWrords) {
        List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByText(kWrords);
        if (list.size() > 0) {
             return true;
        }
        return false;
    }

    private boolean justFindNode(AccessibilityNodeInfo node, String targetText, String targetContentDescription, String targetClassName) {
        if (null != node) {
            int childnum = node.getChildCount();
            if (childnum == 0) {
                CharSequence text = null;
                CharSequence contentDescription = null;
                CharSequence className = node.getClassName();

                if (null != className && className.equals(targetClassName)) {
                    if (null != targetText) {
                        text = node.getText();
                        if (null != text) {
                            if (text.equals(targetText)) {
                                Log.i(TAG, "[justFindNode] tnode text: " + node.getText() + ", contentDescription: " + node.getContentDescription() + ", className: " + node.getClassName());
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else if(null != targetContentDescription) {
                        contentDescription = node.getContentDescription();
                        if (null != contentDescription) {
                            if (contentDescription.equals(targetContentDescription)) {
                                Log.i(TAG, "[justFindNode] tnode text: " + node.getText() + ", contentDescription: " + node.getContentDescription() + ", className: " + node.getClassName());
                                return true;
                            } else {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }
                } else {
                    Log.i(TAG, "[justFindNode] current node className is not target, targetClassName: " + targetClassName);
                    return false;
                }
            } else {
                boolean childFlag = false;
                for (int i = 0; i < childnum; i++) {
                    if (childFlag) {
                        justFindNode(node.getChild(i), targetText, targetContentDescription, targetClassName);
                    } else {
                        childFlag = justFindNode(node.getChild(i), targetText, targetContentDescription, targetClassName);
                    }
                }
                return childFlag;
            }
        }
        return false;
    }

    private boolean findNode2Execute(AccessibilityNodeInfo node, String targetText, String targetContentDescription, String targetClassName, String textLabel) {
        if (null != node) {
            int childnum = node.getChildCount();
            if (childnum == 0) {
                Log.i(TAG, "[findNode2Execute] cnode text: " + node.getText() + ", contentDescription: " + node.getContentDescription() + ", className: " + node.getClassName());
                boolean isTarget = false;
                CharSequence text = null;
                CharSequence contentDescription = null;
                CharSequence className = node.getClassName();

                if (null != className && className.equals(targetClassName)) {
                    if (null != targetText) {
                        text = node.getText();
                        if (null != text) {
                            if (!targetText.equals("搜索:") && text.equals(targetText)) {
                                if (text.equals("设置备注和标签")) {
                                    return true;
                                }

                                isTarget = true;
                            }else if (text.toString().indexOf(targetText) >= 0) {
                                isTarget = true;
                            } else {
                                return false;
                            }
                        } else if (targetText.equals("清除") && node.getPackageName().equals("com.tencent.mm")) {
                            isTarget = true;
                        } else {
                            return false;
                        }
                    } else if(null != targetContentDescription) {
                        contentDescription = node.getContentDescription();
                        if (null != contentDescription) {
                            if (contentDescription.equals(targetContentDescription)) {
                                isTarget = true;
                            } else {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }

                    if (isTarget) {
                        try {
                            sleep((long) ((Math.random() / 3) * 10000));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

//                        Log.i(TAG, node.toString());
//                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        Log.i(TAG, "isClicked is " + node.isClickable());
//                        try{
//                            AccessibilityNodeInfo tn = node.getParent();
//                            int nn = tn.getChildCount();
//                            Log.i(TAG, "nn is " + nn);
//                            for (int n = 0; n < nn; n++) {
//                                Log.i(TAG, "No." + n + " is : " + tn.getChild(n).toString());
//                                tn.getChild(n).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                            }
//
//                            tn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                            tn.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                            tn.getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                            tn.getParent().getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                            tn.getParent().getParent().getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        }catch (Exception e){
//                            Log.i(TAG, e.toString());
//                        }

                        if (targetClassName.equals("android.widget.EditText")) {
                            return findNode2Edit(node, textLabel);
                        }
                        if (node.isClickable()) {
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            return true;
                        } else {
                            try{
                                AccessibilityNodeInfo tnode = node.getParent();
                                while (!tnode.isClickable()) {
                                    tnode = tnode.getParent();
                                }
                                tnode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }catch (Exception e) {
                                Log.i(TAG, e.toString());
                                return false;
                            }

                            return true;
                        }
                    }
                } else {
                    Log.i(TAG, "current node className is not target, targetClassName: " + targetClassName);
                    return false;
                }
            } else {
                boolean childFlag = false;
                for (int i = 0; i < childnum; i++) {
                    if (childFlag) {
                        findNode2Execute(node.getChild(i), targetText, targetContentDescription, targetClassName, textLabel);
                    } else {
                        childFlag = findNode2Execute(node.getChild(i), targetText, targetContentDescription, targetClassName, textLabel);
                    }
                }
                return childFlag;
            }
        }
        return false;
    }

    private boolean findNode2Edit(AccessibilityNodeInfo node, String textLabel) {
        Log.i(TAG, "findNode2Edit enter.");

        ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", textLabel);
        clipBoard.setPrimaryClip(clip);
        if (node.isEditable()) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        } else {
            try{
                AccessibilityNodeInfo tnode = node.getParent();
                while (!tnode.isEditable()) {
                    tnode = tnode.getParent();
                }
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            }catch (Exception e) {
                Log.i(TAG, e.toString());
                return false;
            }
        }
        return true;
    }

    @Override
    public void onInterrupt() {

    }
}
