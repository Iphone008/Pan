package com.example.library.view;


import com.example.library.Constants;
import com.example.library.device.DeviceInfo;
import com.example.library.device.DeviceRuntime;
import com.example.library.interfaces.ComponentAssertion;
import com.example.library.interfaces.IContentScroll.ContentScrollMeasurer;
import com.example.library.interfaces.IContentScroll.PanelHeightMeasurer;
import com.example.library.interfaces.listener.IListener.OnComponentClickListener;
import com.example.library.interfaces.listener.IListener.OnKeyboardStateListener;
import com.example.library.interfaces.listener.IListener.OnPanelChangeListener;
import com.example.library.interfaces.listener.IListener.OnTextFieldChangeListener;
import com.example.library.utils.DisplayUtil;
import com.example.library.utils.PanelUtil;
import com.example.library.view.content.DirectionalContentContainer;
import com.example.library.view.content.IContentContainer;
import com.example.library.view.content.RelativeContentContainer;
import com.example.library.view.content.StackContentContainer;
import com.example.library.view.panel.IPanelComponent;
import com.example.library.view.panel.PanelContainer;
import com.example.library.view.panel.PanelView;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.*;
import ohos.agp.utils.Rect;
import ohos.agp.window.service.Window;
import ohos.agp.window.service.WindowManager;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PanelSwitchLayout extends DirectionalLayout implements ComponentAssertion,ComponentContainer.ArrangeListener{
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0xD001500, "MY_TAG_PanelSwitchLayout");
    private List<OnComponentClickListener> viewClickListeners = new ArrayList<>();
    private List<OnPanelChangeListener> panelChangeListeners = new ArrayList<OnPanelChangeListener>();
    private List<OnKeyboardStateListener> keyboardStatusListeners = new ArrayList<OnKeyboardStateListener>();
    private List<OnTextFieldChangeListener> editFocusChangeListeners = new ArrayList<OnTextFieldChangeListener>();
    List<Component> mPanelLayoutComponent=new ArrayList<>();

    private IContentContainer contentContainer;
    private PanelContainer panelContainer;
    private Window mWindow;
    private Component mComponent;
    private AbilitySlice mAbility;
    private List<ContentScrollMeasurer> contentScrollMeasurers = new ArrayList<>();
    private HashMap panelHeightMeasurers = new HashMap<Integer, PanelHeightMeasurer>();

    private boolean isKeyboardShowing = false;
    private int panelId = Constants.getInstance().PANEL_NONE;
    private int lastPanelId = Constants.getInstance().PANEL_NONE;
    private int lastPanelHeight = -1;
    private int animationSpeed = 200; //standard
    private boolean contentScrollOutsizeEnable = true;

    private DeviceRuntime deviceRuntime = null;
    private Rect realBounds = null;
    private boolean mCheckoutKeyboard = true;
    private EventHandler mEventHandler;
    private PanelSwitchLayout mPanelSwitchLayout =null;


//初始化 PanelSwitchLayout
    public PanelSwitchLayout(Context context) {
        super(context);
        HiLog.debug(LABEL," Run PanelSwitchLayout 1");
    }

    public PanelSwitchLayout(Context context, AttrSet attrSet) {
        super(context, attrSet,"");
        init();
        HiLog.debug(LABEL," Run PanelSwitchLayout 2");
    }

    public PanelSwitchLayout(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init();
        boolean isshow=isComponentDisplayed();
        HiLog.debug(LABEL," Run PanelSwitchLayout 3");
    }
    void init(){
        //设置监听器
        boolean isshow=isComponentDisplayed();
        HiLog.info(LABEL," 当前View是否显示出来   "+isshow);
        setArrangeListener(this);
        mPanelSwitchLayout=this;
    }

    public IContentContainer getContentContainer() {
        return contentContainer;
    }
    Map<Integer,IPanelComponent> mPanelSparseArray=new HashMap<>();
    private static long preClickTime = 0;


    private Runnable keyboardStateRunnable = new Runnable() {
        @Override
        public void run() {
            toKeyboardState(false);
        }
    };

    private boolean doingCheckout = false;
    private CheckoutKbRunnable retryCheckoutKbRunnable = new CheckoutKbRunnable();
    private boolean async = false;
    private boolean hasAttachLister = false;


    public void onFinishInflate(){

        HiLog.error(LABEL,"PanelSwitchLayout  run PanelSwitchLayout  initView ");
        try {
            assertComponent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (hasAttachLister){
            hasAttachLister=true;
        }
        getLayoutType();
        initListener();
    }

    public void setContentScrollOutsizeEnable(boolean enable) {
        this.contentScrollOutsizeEnable = enable;
    }


    public boolean isContentScrollOutsizeEnable() {
        return contentScrollOutsizeEnable;
    }


    private void recycle() {
//        mEventHandler.removeTask(retryCheckoutKbRunnable);
        retryCheckoutKbRunnable.removeCheckoutKbRunnable();
        mEventHandler.removeTask(keyboardStateRunnable);
        contentContainer.getInputActionImpl().recycler();
        if (hasAttachLister) {
                hasAttachLister = false;
        }
    }

    //重载这个函数
    private void checkoutKeyboard() {
        boolean mRetry = true;
        long mDelay = 200L;
//        mEventHandler.removeTask(this::checkoutKeyboard);
        retryCheckoutKbRunnable.removeCheckoutKbRunnable();
        retryCheckoutKbRunnable.retry = mRetry;
        retryCheckoutKbRunnable.delay = mDelay;
        retryCheckoutKbRunnable.run();

    }

    private void checkoutKeyboard(boolean Retry) {
        boolean mRetry = Retry;
        long mDelay = 200L;
        retryCheckoutKbRunnable.removeCheckoutKbRunnable();
        retryCheckoutKbRunnable.retry = mRetry;
        retryCheckoutKbRunnable.delay = mDelay;
        retryCheckoutKbRunnable.run();
    }

    private void checkoutKeyboard(boolean Retry, long Delay) {
        boolean mRetry = Retry;
        long mDelay = Delay;
        mEventHandler.removeTask(retryCheckoutKbRunnable);
        retryCheckoutKbRunnable.retry = mRetry;
        retryCheckoutKbRunnable.delay = mDelay;
        retryCheckoutKbRunnable.run();
    }
    @Override
    public void assertComponent() throws Exception {
        HiLog.debug(LABEL,"获取的当前页面为 getChildCount" +getChildCount());
        if (getChildCount() != 2) {
            throw new RuntimeException("PanelSwitchLayout -- PanelSwitchLayout should has two children,the first is ContentContainer,the other is PanelContainer！");
        }
        HiLog.info(LABEL,"getChildCount  "+getChildCount());
        Component firstView = getComponentAt(0);
        Component secondView = getComponentAt(1);

        if (firstView instanceof IContentContainer) {
            HiLog.info(LABEL,"firstView  is run");
        } else {
            throw new RuntimeException("PanelSwitchLayout -- the first view isn't a IContentContainer");
        }
        contentContainer = (IContentContainer) firstView;
        HiLog.info(LABEL,"assertView  get contentContainer is null "+(contentContainer==null));
        if (secondView instanceof PanelContainer) {
        } else {
            throw new RuntimeException("PanelSwitchLayout -- the second view is a ContentContainer, but the other isn't a PanelContainer！");
        }
        //需要調用一下assert 方法  让当前view可以拿到这个 view
        panelContainer = (PanelContainer) secondView;
        HiLog.info(LABEL,"getPanelLayoutId() --> is size "+getPanelLayoutComponent().size());
        panelContainer.setPanelLayoutId(getPanelLayoutComponent());
    }

    //初始化监听器
    private void initListener() {
        /**
         * 1. if current currentPanelId is None,should show keyboard
         * 2. current currentPanelId is not None or KeyBoard that means some panel is showing,hide it and show keyboard
         *
         */
        contentContainer.getInputActionImpl().setEditTextClickListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                notifyViewClick(component);
                checkoutKeyboard();
            }
        });
        contentContainer.getInputActionImpl().setEditTextFocusChangeListener(new FocusChangedListener() {
            @Override
            public void onFocusChange(Component component, boolean b) {
                notifyEditFocusChange(component, b);
                checkoutKeyboard();
            }
        });

        contentContainer.getResetActionImpl().setResetCallback(()->{
            HiLog.info(LABEL,"初始化 添加页面 重置Panel ---> ");
            hookSystemBackByPanelSwitcher();
        });

        /**
         * save panel that you want to use these to checkout
         */
        mPanelSparseArray = panelContainer.panelComponentMap;
        for (IPanelComponent iPanelComponent:mPanelSparseArray.values()){
            Component keyView=contentContainer.findTriggerView(iPanelComponent.getBindingTriggerViewId());

            keyView.setClickedListener(new ClickedListener() {
                boolean Keyboard=true;
                @Override
                public void onClick(Component component) {
                    HiLog.info(LABEL,"当前点击的事件是  ： "+component.getId());
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - preClickTime <= Constants.getInstance().PROTECT_KEY_CLICK_DURATION) {
                        return;
                    }
                    //提示当前点击事件的view 已经切换了
                    notifyViewClick(component);

                    int targetId = panelContainer.getPanelId(iPanelComponent);
                    HiLog.info(LABEL,"iPanelComponent.isShowing() "+iPanelComponent.isShowing());
                    if (panelId == targetId&&iPanelComponent.isShowing()) {
                        if (Keyboard){
                            HiLog.info(LABEL,"点击事件： ID相同，弹出键盘 "+Keyboard);
                            DisplayUtil.getInstance().showSoftInput();
                            Keyboard=false;
                        }else {
                            HiLog.info(LABEL,"点击事件： ID相同，隐藏键盘 "+Keyboard);
                            DisplayUtil.getInstance().hideSoftInput();
                            Keyboard=true;
                        }
                    } else {
                        checkoutPanel(targetId);
                    }
                    preClickTime=currentTime;
                }
            });
        }

    }

    //判断当前Component的点击状态
   private void HidePanelStatus(int PanelId){
       ((PanelView)mPanelSparseArray.get(PanelId)).setVisibility(Component.HIDE);
   }

    //尝试使用强制转换当前的第一个Component的类型
    private void  getLayoutType(){
        boolean getComponentStyle=false;
        Component component = getComponentAt(0);
        if (component instanceof RelativeContentContainer){
            RelativeContentContainer relativeContentContainer=(RelativeContentContainer)component;
            relativeContentContainer.onFinishFlate();
        }else if(component instanceof StackContentContainer){
            StackContentContainer stackContentContainer=(StackContentContainer)component;
            stackContentContainer.onFinishFlate();
        }else if (component instanceof DirectionalContentContainer){
            DirectionalContentContainer directionalContentContainer=(DirectionalContentContainer)component;
            directionalContentContainer.onFinishInflate();
        }


    }



    //绑定事件  OnViewClickListenerBuilder.OnViewClickListener
    public void bindListener(List<OnComponentClickListener> viewClickListeners, List<OnPanelChangeListener> panelChangeListeners,
                             List<OnKeyboardStateListener> keyboardStatusListeners, List<OnTextFieldChangeListener> editFocusChangeListeners) {
        this.viewClickListeners = viewClickListeners;
        this.panelChangeListeners = panelChangeListeners;
        this.keyboardStatusListeners = keyboardStatusListeners;
        this.editFocusChangeListeners = editFocusChangeListeners;
        //添加PanelLayoutId 进行绘制
    }


    public void setScrollMeasurers(List<ContentScrollMeasurer> list) {
        contentScrollMeasurers.addAll(list);
    }
    public void setPanelHeightMeasurers(List<PanelHeightMeasurer> list) {
        for (PanelHeightMeasurer panelHeightMeasurer : list) {
            panelHeightMeasurers.put(panelHeightMeasurer.getPanelTriggerId(), panelHeightMeasurer);
        }
    }


    /**
     * 常规导航栏显示或者全屏手势虚拟导航栏显示且不通过 SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION 让界面绘制到全屏手势导航栏下时有值
     */
    private int getCurrentNavigationHeight(DeviceRuntime deviceRuntime, DeviceInfo deviceInfo) {
        if (deviceRuntime.isNavigationBarShow) {
            return (int) deviceInfo.getCurrentNavigationBarHeightWhenVisible(deviceRuntime.isPortrait, deviceRuntime.isPad);
        } else {
            return 0;
        }
    }

    /**
     * 历史从 fullScreen api 控制上看，该属性决定状态栏行为。
     * 对于有导航栏的机型，使用该属性进行全屏显示不可取。
     */
    private int getCurrentStatusBarHeight(DeviceInfo deviceInfo) {
        HiLog.info(LABEL,"getCurrentStatusBarHeight:  is run ");
        return deviceInfo.getStatusBarH();
    }


    private int lastContentHeight;
    private boolean lastNavigationBarShow;
    private int lastKeyboardHeight;
    private int minLimitOpenKeyboardHeight=300;
    private int minLimitCloseKeyboardHeight;
    int sumI=0;

//绑定视图  添加 panel
    public void bindWindow(Ability ability, Window window,Component component) {
        DisplayUtil.hideSoftInput();
        HiLog.info(LABEL," PanelSwitchLayout ability  is run   "+(ability==null));
        deviceRuntime = new DeviceRuntime(ability, window,component);
        HiLog.info(LABEL,"deviceRuntime  is null "+deviceRuntime.toString());
        if (deviceRuntime != null) {
            window.setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_RESIZE);
            contentContainer.getInputActionImpl().updateFullScreenParams(deviceRuntime.isFullScreen, panelId, getCompatPanelHeight(panelId));
             this.setLayoutRefreshedListener(new LayoutRefreshedListener() {
                 @Override
                 public void onRefreshed(Component component) {


                     HiLog.info(LABEL, "onGlobalLayout:  界面每一次变化的信息回调 "+component.getId());

                     int screenHeight = DisplayUtil.getScreenRealHeight(mContext);//获取屏幕高度
                     HiLog.info(LABEL,"获取的屏幕高度 ："+screenHeight);
                     int contentHeight = (int)DisplayUtil.getHeight(mContext)-Constants.NavigationBarHeght;//获取内容区域的高度 默认的是存在虚拟导航栏 p40 126
                     HiLog.info(LABEL,"获取的内容高度 "+contentHeight);
                     DeviceInfo info = deviceRuntime.getDeviceInfoByOrientation(mPanelSwitchLayout,true);
                     HiLog.info(LABEL,"DeviceInfo info  "+info.toString());
                     int curStatusHeight = getCurrentStatusBarHeight(info);
                     int cusNavigationHeight = getCurrentNavigationHeight(deviceRuntime, info);
                     int systemUIHeight = curStatusHeight + cusNavigationHeight;
                     HiLog.info(LABEL,"DeviceInfo systemUIHeight  "+systemUIHeight);
                     HiLog.info(LABEL,"-----------------------------------------------bindwindow--------------------");

                     lastNavigationBarShow = deviceRuntime.isNavigationBarShow;
                     int keyboardHeight = 300;
                     HiLog.info(LABEL,"DeviceInfo keyboardHeight  "+keyboardHeight);
                     int NavigationBarH = (int) info.getNavigationBarH();//获取虚拟导航栏高度
                     int realHeight = keyboardHeight + NavigationBarH;
                     minLimitCloseKeyboardHeight = (int)info.getNavigationBarH();
                     HiLog.info(LABEL,"isKeyboardShowing  :  "+isKeyboardShowing);
                     if (isKeyboardShowing) {
                         if (keyboardHeight <= minLimitOpenKeyboardHeight) {
                             isKeyboardShowing = false;
                             if (isKeyboardState()) {
                                 checkoutPanel(Constants.PANEL_NONE);
                             }
                             notifyKeyboardState(false);
                         } else {
                             /**
                              * 拉起输入法的时候递增，隐藏输入法的时候递减，机型较差的手机需要 requestLayout() 动态更新布局
                              */
                             if (keyboardHeight != lastKeyboardHeight) {
                                 PanelUtil.getInstance().setKeyBoardHeight(mAbility, realHeight);
                                 postLayout();
                             }
                         }
                     } else {
                         HiLog.info(LABEL,"当前的keyboardHeight : "+keyboardHeight);
                         HiLog.info(LABEL,"当前的minLimitOpenKeyboardHeight  : "+minLimitOpenKeyboardHeight);
                         if (keyboardHeight > minLimitOpenKeyboardHeight) {
                             isKeyboardShowing = true;
                             if (keyboardHeight > lastKeyboardHeight) {
                                 HiLog.info(LABEL, "onGlobalLayout: $TAG#onGlobalLayout try to set KeyBoardHeight : $realHeight，isShow $isKeyboardShowing");
                                 PanelUtil.getInstance().setKeyBoardHeight(mAbility, realHeight);
                                 postLayout();
                             }
                             if (!isKeyboardState()) {
                                 checkoutPanel(Constants.getInstance().PANEL_KEYBOARD, false);
                             }
                             notifyKeyboardState(true);
                         } else {
                             //1.3.5 实时兼容导航栏动态隐藏调整布局
                             String v = lastContentHeight + "";

                             //出现问题。
                             HiLog.info(LABEL,"v "+v);
                             if (!v.equals("")) {
                                 String mv = lastNavigationBarShow + "";
                                 if (!mv.equals("")) {
                                     if (lastContentHeight != contentHeight && lastNavigationBarShow != deviceRuntime.isNavigationBarShow) {
                                         postLayout();
                                     }
                                 }
                             }
                         }
                     }
                     lastKeyboardHeight = keyboardHeight;
                     lastContentHeight = contentHeight;
                 }
             });
            hasAttachLister=true;

        }
    }



    @Override
    public boolean onArrange(int l, int t, int r, int b) {
        int visibiliy = getVisibility();
        int contentContainerTop = 0;
        int contentContainerHeight = 0;
        int panelContainerTop = 0;
        int compatPanelHeight = 0;
        if (visibiliy != Component.VISIBLE) {
            return true;
        }
        if (deviceRuntime != null) {
            HiLog.info(LABEL,"deviceRuntime not null");
            DeviceInfo deviceInfo = deviceRuntime.getDeviceInfoByOrientation(mPanelSwitchLayout);
            /**
             * 当还没有进行输入法高度获取时，由于兼容性测试之后设置的默认高度无法兼容所有机型
             * 为了业务能100%兼容，开放设置每个面板的默认高度，待输入法高度获取之后统一高度。
             */
            compatPanelHeight = getCompatPanelHeight(panelId);

            int paddingTop = getPaddingTop();
            int allHeight = deviceInfo.getScreenH();
            HiLog.info(LABEL,"deviceInfo onArrage( --> changed  l "+l+" t "+t+" r "+r+" b "+b);
            HiLog.info(LABEL,"获取的所有高度  allHeight :  "+deviceRuntime.isNavigationBarShow);
            //状态栏  是否显示
            String thisPanelStatus="";
            if (Constants.DEBUG){
                switch (panelId){
                    case Constants.PANEL_NONE:
                        thisPanelStatus="收起所有输入源";
                        break;
                    case Constants.PANEL_KEYBOARD:
                        thisPanelStatus="显示键盘输入";
                        break;
                    default:
                        thisPanelStatus="显示面板输入";
                        break;
                }
                HiLog.info(LABEL,"当前面板状态 ============>"+thisPanelStatus);
            }
            //获取虚拟键盘是否存在
            if (deviceRuntime.isNavigationBarShow) {
                /**
                 * 1.1.0 使用 screenWithoutNavigationHeight + navigationBarHeight ，结合 navigationBarShow 来动态计算高度，但是部分特殊机型
                 * 比如水滴屏，刘海屏，等存在刘海区域，甚至华为，小米支持动态切换刘海模式（不隐藏刘海，隐藏后状态栏在刘海内，隐藏后状态栏在刘海外）
                 * 同时还存在全面屏，挖孔屏，这套方案存在兼容问题。
                 * CusShortUtil 支持计算绝大部分机型的刘海高度，但是考虑到动态切换的模式计算太过于复杂，且不能完全兼容所有场景。
                 * 1.1.1 使用 screenHeight - navigationBarHeight，结合 navigationBarShow 来动态计算告诉，原因是：
                 * 无论现不现实刘海区域，只需要记住应用的绘制区域以 getDecorView 的绘制区域为准，我们只需要关注一个关系：
                 * 刘海区域与状态栏区域的是否重叠。
                 * 如果状态栏与刘海不重叠，则 screenHeight 不包含刘海
                 * 如果状态栏与刘海重叠，则 screenHeight 包含刘海
                 * 这样抽象逻辑变得更加简单。
                 */
                HiLog.info(LABEL,"deviceRuntime.isPortrait  "+deviceRuntime.isPortrait);
                HiLog.info(LABEL,"deviceRuntime.isPad  "+deviceRuntime.isPad);
                allHeight -= deviceInfo.getCurrentNavigationBarHeightWhenVisible(deviceRuntime.isPortrait, deviceRuntime.isPad);
                HiLog.info(LABEL,"allHeight "+allHeight);
            }

            HiLog.info(LABEL,"mComponent  is null :  "+(mComponent==null));
            int[] localLocation = DisplayUtil.getLocationOnScreen(this);
            HiLog.info(LABEL,"localLocation :  "+localLocation+" localLocation  size  is : "+localLocation.length);
            allHeight -= localLocation[1];
            HiLog.info(LABEL,"allHeight  "+allHeight);

            contentContainerTop = getContentContainerTop(compatPanelHeight);
            contentContainerTop += paddingTop;
            contentContainerHeight = getContentContainerHeight(allHeight, paddingTop, compatPanelHeight);
            panelContainerTop = contentContainerTop + contentContainerHeight;

            boolean changeBounds = isBoundChange(l, contentContainerTop, r, panelContainerTop + compatPanelHeight);
            HiLog.info(LABEL, "onLayout:  changeBounds " + changeBounds);
            if (changeBounds) {
                boolean reverseResetState = reverseResetState();
                HiLog.info(LABEL, "onLayout:  reverseResetState " + reverseResetState);
                if (reverseResetState) {
                    setTransition(animationSpeed, panelId);
                }
            } else {
//                如果功能面板的互相切换，则需要判断是否存在高度不一致，如果不一致则需要过渡
                HiLog.info(LABEL, "onLayout: lastPanelHeight:" + lastPanelHeight+", compatPanelHeight:"+compatPanelHeight);
                if (lastPanelHeight != -1 && lastPanelHeight != compatPanelHeight) {
                    HiLog.info(LABEL,"panelId ");
                    setTransition(animationSpeed, panelId);
                }
            }
            //添加Panel Layout Id

            contentContainer.layoutContainer(l, contentContainerTop, r, contentContainerTop + contentContainerHeight,
                          contentScrollMeasurers, compatPanelHeight, contentScrollOutsizeEnable, isResetState());
            contentContainer.changeContainerHeight(contentContainerHeight);
            panelContainer.changeContainerHeight(compatPanelHeight);
            this.lastPanelHeight = compatPanelHeight;

            contentContainer.getInputActionImpl().updateFullScreenParams(deviceRuntime.isFullScreen, panelId, compatPanelHeight);

        }

        return false;
    }






    private int getContentContainerTop(int scrollOutsideHeight) {
        int result;
        if (contentScrollOutsizeEnable) {
            if (isResetState()) {
                result = 0;
            } else {
                result = -scrollOutsideHeight;
            }
        } else {
            result = 0;
        }
        HiLog.info(LABEL, "getContentContainerTop: " + result);
        return result;
    }

    private int getContentContainerHeight(int allHeight, int paddingTop, int scrollOutsideHeight) {
        int result;
        if (!contentScrollOutsizeEnable && !isResetState()) result = scrollOutsideHeight;
        else result = 0;
        return allHeight - paddingTop - result;
    }

    private boolean isBoundChange(int l, int t, int r, int b) {
        HiLog.info(LABEL,"isBoundChange  is run  l"+l);
        HiLog.info(LABEL,"isBoundChange  is run  t"+t);
        HiLog.info(LABEL,"isBoundChange  is run  r"+r);
        HiLog.info(LABEL,"isBoundChange  is run  b"+b);
        boolean change;
        if (realBounds == null || realBounds.left != l || realBounds.top != t || realBounds.right != r || realBounds.bottom != b) {
            change = true;
        } else {
            change = false;
        }
        realBounds = new Rect(l, t, r, b);
        HiLog.info(LABEL,"isBoundChange  is  change --->"+realBounds);
        return change;
    }


    public void toKeyboardState(boolean async) {
        if (async) {
//            post(keyboardStateRunnable)
            keyboardStateRunnable.run();
//            postLayout();
        } else {
            contentContainer.getInputActionImpl().requestKeyboard();
        }
    }

    /**设置当前Panel的大小
     * @param panelId
     * @return
     */
    public boolean checkoutPanel(int panelId) {
        HiLog.info(LABEL,"get checkoutPanel panelId is run  "+panelId);
        HiLog.info(LABEL,"get checkoutPanel doingCheckout  "+doingCheckout);
        if (doingCheckout) {
            return false;
        }
        HiLog.info(LABEL,"get checkoutPanel doingCheckout  "+doingCheckout);
        doingCheckout = true;
        HiLog.info(LABEL,"get checkoutPanel this.panelId  "+this.panelId);
        if (panelId == this.panelId) {
            doingCheckout = false;
            return false;
        }
        switch (panelId) {
            case Constants.PANEL_NONE:
                HiLog.info(LABEL,"PANEL_NONE is run 参数一  ");
//                contentContainer.getInputActionImpl().hideKeyboard(true);
//                contentContainer.getResetActionImpl().enableReset(false);
                break;
            case Constants.PANEL_KEYBOARD:
                HiLog.info(LABEL,"PANEL_KEYBOARD is run 参数一  ");
                if (mCheckoutKeyboard) {
                    if (!contentContainer.getInputActionImpl().showKeyboard()) {
                        doingCheckout = false;
                        return false;
                    }
                }
                contentContainer.getResetActionImpl().enableReset(true);
                break;
            default:
                HiLog.info(LABEL,"default is run 参数一  ");
                HiLog.info(LABEL,"checkoutPanel --> getWidth() ,"+getWidth()+" getPaddingLeft() ,"+getPaddingLeft()+" getPaddingRight() ,"+getPaddingRight());
                HiLog.info(LABEL,"getCompatPanelHeight(panelId)  ,"+getCompatPanelHeight(panelId));
                Pair size = new Pair(getWidth() - getPaddingLeft() - getPaddingRight(), getCompatPanelHeight(panelId));//当前页面
                Pair oldSize = panelContainer.showPanel(panelId, size);
                HiLog.info(LABEL,"----->"+oldSize.toString());
                if (size.f != oldSize.f || size.s != oldSize.s) {
                    notifyPanelSizeChange(panelContainer.getPanelView(panelId), DisplayUtil.getInstance().isPortrait(mContext), (int) oldSize.f, (int) oldSize.s, (int) size.f, (int) size.s);
                }
                contentContainer.getInputActionImpl().hideKeyboard(false);
                contentContainer.getResetActionImpl().enableReset(true);
                break;
        }

        this.lastPanelId = this.panelId;
        HiLog.info(LABEL,"lastPanelId  ： "+lastPanelId);
        this.panelId = panelId;
        //强制刷新页面
        postLayout();
        notifyPanelChange(this.panelId);
        doingCheckout = false;
        return true;
    }
    //隐藏Panel
    public  void  HidePanelView(int PanelID){
        IPanelComponent component=mPanelSparseArray.get(PanelID);
        ((PanelView)component).setVisibility(Component.HIDE);
    }

    public boolean checkoutPanel(int panelId, boolean checkoutKeyboard) {
        HiLog.info(LABEL,"checkoutPanel 传入的 panelId ： "+panelId);
        mCheckoutKeyboard = checkoutKeyboard;
        HiLog.info(LABEL, "checkoutPanel: panelId and  checkoutKeyboard  is run ");
        if (doingCheckout) {
            HiLog.info(LABEL, "checkoutPanel: is checkouting,just ignore!");
            return false;
        }
        doingCheckout = true;
        if (panelId == this.panelId) {
            HiLog.info(LABEL, "checkoutPanel: current panelId is " + panelId + ",just ignore!");
            doingCheckout = false;
            return false;
        }
        switch (panelId) {
            case Constants.PANEL_NONE:
                //隐藏键盘
                HiLog.info(LABEL, "PANEL_NONE   is run");
                DisplayUtil.hideSoftInput();
                contentContainer.getInputActionImpl().hideKeyboard(true);
                contentContainer.getResetActionImpl().enableReset(false);
                break;
            case Constants.PANEL_KEYBOARD:
                if (mCheckoutKeyboard) {
                    if (!contentContainer.getInputActionImpl().showKeyboard()) {
                        HiLog.info(LABEL, "checkoutPanel: system show keyboard fail, just ignore! ");
                        doingCheckout = false;
                        return false;
                    }
                }
                contentContainer.getResetActionImpl().enableReset(true);
                break;
            default:
                Pair size = new Pair(getEstimatedWidth() - getPaddingLeft() - getPaddingRight(), getCompatPanelHeight(panelId));
                Pair oldSize = panelContainer.showPanel(panelId, size);
                if (size.f != oldSize.f || size.s != oldSize.s) {
                    notifyPanelSizeChange(panelContainer.getPanelView(panelId), DisplayUtil.getInstance().isPortrait(mContext), (int) oldSize.f, (int) oldSize.s, (int) size.f, (int) size.s);
                }
                //隐藏键盘
                DisplayUtil.hideSoftInput();
                contentContainer.getInputActionImpl().hideKeyboard(false);
                contentContainer.getResetActionImpl().enableReset(true);
                break;
        }
        HiLog.info(LABEL,"lastPanelId  ---- "+lastPanelId);
        HiLog.info(LABEL,"panelId ------"+this.panelId);
        this.lastPanelId = this.panelId;

        this.panelId = panelId;
        HiLog.info(LABEL, "checkoutPanel: checkout success ! ");
        postLayout();
        notifyPanelChange(this.panelId);
        doingCheckout = false;
        return true;
    }

    public boolean isPanelState() {
        return isPanelState(panelId);
    }

    public boolean isKeyboardState() {
        return isKeyboardState(panelId);
    }

    public boolean isResetState() {
        HiLog.info(LABEL, "isResetState:   panelId  是 " + panelId);
        return isResetState(panelId);
    }

    private boolean isKeyboardState(int panelId) {
        HiLog.info(LABEL,"isKeyboardState  当前ID  "+panelId);
        return panelId == Constants.PANEL_KEYBOARD ? true : false;
    }

    private boolean isPanelState(int panelId) {
        return !isResetState(panelId) && !isKeyboardState(panelId);
    }

    private boolean isResetState(int panelId) {
        HiLog.info(LABEL, "isResetState: panelId  " + panelId);
        return panelId == Constants.PANEL_NONE ? true : false;
    }

    private int getCompatPanelHeight(int panelId) {
        HiLog.info(LABEL,"getCompatPanelHeight:  is run ");
        HiLog.info(LABEL,"getCompatPanelHeight  panelId  "+panelId);
        HiLog.info(LABEL,"getCompatPanelHeight  isPanelState(panelId)  "+isPanelState(panelId));
        if (isPanelState(panelId)) {
            PanelHeightMeasurer panelHeightMeasurer = (PanelHeightMeasurer) panelHeightMeasurers.get(panelId);
            if (panelHeightMeasurer != null) {
                HiLog.info(LABEL,"panelHeightMeasurer : "+panelHeightMeasurer);
                //如果输入法还没有测量或者不同步输入法高度，则是有默认高度 v hasMeasuredKeyboard(getContext()
                if (!PanelUtil.getInstance().hasMeasuredKeyboard(mContext) || !panelHeightMeasurer.synchronizeKeyboardHeight()) {
                    int result = panelHeightMeasurer.getTargetPanelDefaultHeight();
                    HiLog.info(LABEL, "getCompatPanelHeight: getCompatPanelHeight by default panel " + result);
                    return result;
                }

            }

        }
        int result = PanelUtil.getInstance().getKeyBoardHeight(mContext);
        HiLog.info(LABEL, "getCompatPanelHeight: " + result);
        return result;
    }

    private void notifyPanelSizeChange(IPanelComponent panelView, boolean portrait, int oldWidth, int oldHeight, int width, int height) {

        if (panelChangeListeners != null) {
            for (OnPanelChangeListener item : panelChangeListeners) {
                item.onPanelSizeChange(panelView, portrait, oldWidth, oldHeight, width, height);
            }
        }

    }

    private void notifyPanelChange(int panelId) {
        HiLog.info(LABEL,"notifyPanelChange is run ");
        if (panelChangeListeners != null) {
            HiLog.info(LABEL,"panelChangeListeners  not null");
            for (OnPanelChangeListener listener : panelChangeListeners) {
                switch (panelId) {
                    case Constants.PANEL_NONE:
                        HiLog.info(LABEL,"notifyPanelChange  PANEL_NONE  is run");
                        listener.onNone();
                        break;
                    case Constants.PANEL_KEYBOARD:
                        listener.onKeyboard();
                        break;
                    default:
                        listener.onPanel(panelContainer.getPanelView(panelId));
                        break;
                }
            }
        }
    }


    private void notifyViewClick(Component component) {
        if (viewClickListeners != null) {
            for (OnComponentClickListener item : viewClickListeners) {
                item.onClickBefore(component);
            }
        }

    }

    private void notifyEditFocusChange(Component component, boolean hasFocus) {
        if (editFocusChangeListeners != null) {
            for (OnTextFieldChangeListener item : editFocusChangeListeners) {
                item.onFocusChange(component, hasFocus);
            }
        }
    }

    /**
     * This will be called when User press System Back Button.
     * 1. if keyboard is showing, should be hide;
     * 2. if you want to hide panel(exclude keyboard),you should call it before [android.support.v7.app.AppCompatActivity.onBackPressed] to hook it.
     *
     * @return if need hook
     */


    public boolean hookSystemBackByPanelSwitcher() {
        HiLog.info(LABEL,"hookSystemBackByPanelSwitcher  is run ");
        HiLog.info(LABEL,"!isResetState()   "+!isResetState());
        if (!isResetState()) {
            //模仿系统输入法隐藏，如果直接掉  checkoutPanel(Constants.PANEL_NONE)，可能导致隐藏时上层 recyclerview 因为 layout 导致界面出现短暂卡顿。
            if (isKeyboardState()) {
                if (isKeyboardShowing) {
                    contentContainer.getInputActionImpl().hideKeyboard(true);
                } else {
                    checkoutPanel(Constants.PANEL_NONE);
                    return false;
                }
            } else {
                checkoutPanel(Constants.PANEL_NONE);
            }
            return true;
        }
        return false;
    }

    private void notifyKeyboardState(boolean visible) {
        if (keyboardStatusListeners != null) {
            for (OnKeyboardStateListener item : keyboardStatusListeners) {
                item.onKeyboardChange(visible, visible == true ? PanelUtil.getInstance().getKeyBoardHeight(mContext) : 0);
            }
        }
    }

    private boolean reverseResetState() {
        return (isResetState(lastPanelId) && !isResetState(panelId)) || (!isResetState(lastPanelId) && isResetState(panelId));
    }


    private void setTransition(long duration, int panelId){
        AnimatorProperty animatorProperty=this.createAnimatorProperty();
        animatorProperty.setDuration(duration);
        animatorProperty.start();
    }

    public  void  setPanelLayoutId(List<Component> panelLayoutId){
        mPanelLayoutComponent =panelLayoutId;
    }
    public List<Component> getPanelLayoutComponent(){
        return mPanelLayoutComponent;
    }


    class CheckoutKbRunnable implements Runnable {
        public boolean retry = false;
        EventHandler eventHandler;
        public long delay = 0L;
        @Override
        public void run() {
            boolean result = checkoutPanel(Constants.getInstance().PANEL_KEYBOARD);
            HiLog.info(LABEL,"result "+result);
            if (!result && panelId != Constants.PANEL_KEYBOARD && retry) {
                eventHandler = new EventHandler(EventRunner.getMainEventRunner());
                eventHandler.postTask(this, delay);
                HiLog.info(LABEL, "" + eventHandler.isIdle());
            }
            retry = false;
        }

        public void removeCheckoutKbRunnable(){
            if (eventHandler!=null){
                eventHandler.removeTask(this::run);
            }
        }
    }


}
