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


    //????????? PanelSwitchLayout
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
        //???????????????
        boolean isshow=isComponentDisplayed();
        System.out.println(" ??????View??????????????????   "+isshow);
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

    //??????????????????
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
       System.out.println("???????????????????????? getChildCount" +getChildCount());
        if (getChildCount() != 2) {
            throw new RuntimeException("PanelSwitchLayout -- PanelSwitchLayout should has two children,the first is ContentContainer,the other is PanelContainer???");
        }
        System.out.println("getChildCount  "+getChildCount());
        Component firstView = getComponentAt(0);
        Component secondView = getComponentAt(1);

        if (firstView instanceof IContentContainer) {
            System.out.println("firstView  is run");
        } else {
            throw new RuntimeException("PanelSwitchLayout -- the first view isn't a IContentContainer");
        }
        contentContainer = (IContentContainer) firstView;
        System.out.println("assertView  get contentContainer is null "+(contentContainer==null));
        if (secondView instanceof PanelContainer) {
        } else {
            throw new RuntimeException("PanelSwitchLayout -- the second view is a ContentContainer, but the other isn't a PanelContainer???");
        }
        //??????????????????assert ??????  ?????????view?????????????????? view
        panelContainer = (PanelContainer) secondView;
        System.out.println("getPanelLayoutId() --> is size "+getPanelLayoutComponent().size());
        panelContainer.setPanelLayoutId(getPanelLayoutComponent());
    }
    private void  getLayoutType(){
        Component component = getComponentAt(0);
        if (component instanceof RelativeContentContainer){
            System.out.println("pppppppp   RelativeContentContainer is run  ");
            RelativeContentContainer relativeContentContainer=(RelativeContentContainer)component;
            relativeContentContainer.onFinishFlate();
        }else if(component instanceof StackContentContainer){
            System.out.println("pppppppp   StackContentContainer is run  ");
            StackContentContainer stackContentContainer=(StackContentContainer)component;
            stackContentContainer.onFinishFlate();
        }else if (component instanceof DirectionalContentContainer){
            System.out.println("pppppppp   DirectionalContentContainer is run  ");
            DirectionalContentContainer directionalContentContainer=(DirectionalContentContainer)component;
            directionalContentContainer.onFinishInflate();
        }


    }

    //??????????????????
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
            System.out.println("????????? ???????????? ??????Panel ---> ");
            hookSystemBackByPanelSwitcher();
        });

        /**
         * save panel that you want to use these to checkout
         */
        mPanelSparseArray = panelContainer.panelComponentMap;
        System.out.println("mPanelSparseArray ????????????  "+mPanelSparseArray.size());
        for (IPanelComponent iPanelComponent:mPanelSparseArray.values()){
            Component keyView=contentContainer.findTriggerView(iPanelComponent.getBindingTriggerViewId());
                System.out.println("keyView is  null "+(keyView==null));
                keyView.setClickedListener(new ClickedListener() {
                boolean Keyboard=true;
                @Override
                public void onClick(Component component) {
                    System.out.println("????????????????????????  ??? "+component.getId());
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - preClickTime <= Constants.getInstance().PROTECT_KEY_CLICK_DURATION) {
                        return;
                    }
                    //???????????????????????????view ???????????????
                    notifyViewClick(component);

                    int targetId = panelContainer.getPanelId(iPanelComponent);

                    if (panelId == targetId&&iPanelComponent.isShowing() ) {
                        boolean Keyboard=true;
                        System.out.println("???????????? ---------checkoutKeyboard??????1----");
                        if (Keyboard){
                            DisplayUtil.getInstance().showSoftInput();
                            Keyboard=false;
                        }else {
                            DisplayUtil.getInstance().hideSoftInput();
                            Keyboard=true;
                        }
                    } else {
                        System.out.println("???????????? ---------checkoutPanel??????2----");
                        DisplayUtil.getInstance().hideSoftInput();
                        checkoutPanel(targetId);
                    }
                    preClickTime=currentTime;
                }
            });
        }

    }


    //????????????  OnViewClickListenerBuilder.OnViewClickListener
    public void bindListener(List<OnComponentClickListener> viewClickListeners, List<OnPanelChangeListener> panelChangeListeners,
                             List<OnKeyboardStateListener> keyboardStatusListeners, List<OnTextFieldChangeListener> editFocusChangeListeners) {
        this.viewClickListeners = viewClickListeners;
        this.panelChangeListeners = panelChangeListeners;
        this.keyboardStatusListeners = keyboardStatusListeners;
        this.editFocusChangeListeners = editFocusChangeListeners;
        //??????PanelLayoutId ????????????
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
     * ???????????????????????????????????????????????????????????????????????? SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION ???????????????????????????????????????????????????
     */
    private int getCurrentNavigationHeight(DeviceRuntime deviceRuntime, DeviceInfo deviceInfo) {
        if (deviceRuntime.isNavigationBarShow) {
            return (int) deviceInfo.getCurrentNavigationBarHeightWhenVisible(deviceRuntime.isPortrait, deviceRuntime.isPad);
        } else {
            return 0;
        }
    }

    /**
     * ????????? fullScreen api ????????????????????????????????????????????????
     * ???????????????????????????????????????????????????????????????????????????
     */
    private int getCurrentStatusBarHeight(DeviceInfo deviceInfo) {
        System.out.println("getCurrentStatusBarHeight:  is run ");
        return deviceInfo.getStatusBarH();
    }


    private int lastContentHeight;
    private boolean lastNavigationBarShow;
    private int lastKeyboardHeight;
    private int minLimitOpenKeyboardHeight=300;
    private int minLimitCloseKeyboardHeight;
    int sumI=0;

    //????????????  ?????? panel
    public void bindWindow(Ability ability, Window window,Component component) {
        DisplayUtil.hideSoftInput();
        System.out.println(" PanelSwitchLayout ability  is run   "+(ability==null));
        deviceRuntime = new DeviceRuntime(ability, window,component);
        System.out.println("deviceRuntime  is null "+deviceRuntime.toString());
        if (deviceRuntime != null) {
            window.setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_RESIZE);
            contentContainer.getInputActionImpl().updateFullScreenParams(deviceRuntime.isFullScreen, panelId, getCompatPanelHeight(panelId));
            this.setLayoutRefreshedListener(new LayoutRefreshedListener() {
                @Override
                public void onRefreshed(Component component) {


                    System.out.println( "onGlobalLayout:  ???????????????????????????????????? "+component.getId());

                    int screenHeight = DisplayUtil.getScreenRealHeight(mContext);//??????????????????
                    System.out.println("????????????????????? ???"+screenHeight);
                    int contentHeight = (int)DisplayUtil.getHeight(mContext)-Constants.NavigationBarHeght;//??????????????????????????? ????????????????????????????????? p40 126
                    System.out.println("????????????????????? "+contentHeight);
                    DeviceInfo info = deviceRuntime.getDeviceInfoByOrientation(mPanelSwitchLayout,true);
                    System.out.println("DeviceInfo info  "+info.toString());
                    int curStatusHeight = getCurrentStatusBarHeight(info);
                    int cusNavigationHeight = getCurrentNavigationHeight(deviceRuntime, info);
                    int systemUIHeight = curStatusHeight + cusNavigationHeight;
                    System.out.println("DeviceInfo systemUIHeight  "+systemUIHeight);
                    System.out.println("-----------------------------------------------bindwindow--------------------");

                    lastNavigationBarShow = deviceRuntime.isNavigationBarShow;
                    int keyboardHeight = 300;
                    System.out.println("DeviceInfo keyboardHeight  "+keyboardHeight);
                    int NavigationBarH = (int) info.getNavigationBarH();//???????????????????????????
                    int realHeight = keyboardHeight + NavigationBarH;
                    minLimitCloseKeyboardHeight = (int)info.getNavigationBarH();
                    System.out.println("isKeyboardShowing  :  "+isKeyboardShowing);
                    if (isKeyboardShowing) {
                        if (keyboardHeight <= minLimitOpenKeyboardHeight) {
                            isKeyboardShowing = false;
                            if (isKeyboardState()) {
                                checkoutPanel(Constants.PANEL_NONE);
                            }
                            notifyKeyboardState(false);
                        } else {
                            /**
                             * ????????????????????????????????????????????????????????????????????????????????????????????? requestLayout() ??????????????????
                             */
                            if (keyboardHeight != lastKeyboardHeight) {
                                PanelUtil.getInstance().setKeyBoardHeight(mAbility, realHeight);
                                postLayout();
                            }
                        }
                    } else {
                        System.out.println("?????????keyboardHeight : "+keyboardHeight);
                        System.out.println("?????????minLimitOpenKeyboardHeight  : "+minLimitOpenKeyboardHeight);
                        if (keyboardHeight > minLimitOpenKeyboardHeight) {
                            isKeyboardShowing = true;
                            if (keyboardHeight > lastKeyboardHeight) {
                                System.out.println( "onGlobalLayout: $TAG#onGlobalLayout try to set KeyBoardHeight : $realHeight???isShow $isKeyboardShowing");
                                PanelUtil.getInstance().setKeyBoardHeight(mAbility, realHeight);
                                postLayout();
                            }
                            if (!isKeyboardState()) {
                                checkoutPanel(Constants.getInstance().PANEL_KEYBOARD, false);
                            }
                            notifyKeyboardState(true);
                        } else {
                            //1.3.5 ?????????????????????????????????????????????
                            String v = lastContentHeight + "";

                            //???????????????
                            System.out.println("v "+v);
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
            System.out.println("deviceRuntime not null");
            DeviceInfo deviceInfo = deviceRuntime.getDeviceInfoByOrientation(mPanelSwitchLayout);
            /**
             * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
             * ???????????????100%????????????????????????????????????????????????????????????????????????????????????????????????
             */
            compatPanelHeight = getCompatPanelHeight(panelId);

            int paddingTop = getPaddingTop();
            int allHeight = deviceInfo.getScreenH();
            System.out.println("deviceInfo onArrage( --> changed  l "+l+" t "+t+" r "+r+" b "+b);
            System.out.println("?????????????????????  allHeight :  "+deviceRuntime.isNavigationBarShow);
            //?????????  ????????????
            String thisPanelStatus="";
            if (Constants.DEBUG){
                switch (panelId){
                    case Constants.PANEL_NONE:
                        thisPanelStatus="?????????????????????";
                        break;
                    case Constants.PANEL_KEYBOARD:
                        thisPanelStatus="??????????????????";
                        break;
                    default:
                        thisPanelStatus="??????????????????";
                        break;
                }
                System.out.println("?????????????????? ============>"+thisPanelStatus);
            }
            //??????????????????????????????
            if (deviceRuntime.isNavigationBarShow) {
                /**
                 * 1.1.0 ?????? screenWithoutNavigationHeight + navigationBarHeight ????????? navigationBarShow ????????????????????????????????????????????????
                 * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                 * ????????????????????????????????????????????????????????????????????????
                 * CusShortUtil ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                 * 1.1.1 ?????? screenHeight - navigationBarHeight????????? navigationBarShow ????????????????????????????????????
                 * ???????????????????????????????????????????????????????????????????????? getDecorView ????????????????????????????????????????????????????????????
                 * ????????????????????????????????????????????????
                 * ??????????????????????????????????????? screenHeight ???????????????
                 * ???????????????????????????????????? screenHeight ????????????
                 * ???????????????????????????????????????
                 */
                System.out.println("deviceRuntime.isPortrait  "+deviceRuntime.isPortrait);
                System.out.println("deviceRuntime.isPad  "+deviceRuntime.isPad);
                allHeight -= deviceInfo.getCurrentNavigationBarHeightWhenVisible(deviceRuntime.isPortrait, deviceRuntime.isPad);
                System.out.println("allHeight "+allHeight);
            }

            System.out.println("mComponent  is null :  "+(mComponent==null));
            int[] localLocation = DisplayUtil.getLocationOnScreen(this);
            System.out.println("localLocation :  "+localLocation+" localLocation  size  is : "+localLocation.length);
            allHeight -= localLocation[1];
            System.out.println("allHeight  "+allHeight);

            contentContainerTop = getContentContainerTop(compatPanelHeight);
            contentContainerTop += paddingTop;
            contentContainerHeight = getContentContainerHeight(allHeight, paddingTop, compatPanelHeight);
            panelContainerTop = contentContainerTop + contentContainerHeight;

            boolean changeBounds = isBoundChange(l, contentContainerTop, r, panelContainerTop + compatPanelHeight);
            System.out.println( "onLayout:  changeBounds " + changeBounds);
            if (changeBounds) {
                boolean reverseResetState = reverseResetState();
                System.out.println( "onLayout:  reverseResetState " + reverseResetState);
                if (reverseResetState) {
                    setTransition(animationSpeed, panelId);
                }
            } else {
//                ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                System.out.println( "onLayout: lastPanelHeight:" + lastPanelHeight+", compatPanelHeight:"+compatPanelHeight);
                if (lastPanelHeight != -1 && lastPanelHeight != compatPanelHeight) {
                    System.out.println("panelId ");
                    setTransition(animationSpeed, panelId);
                }
            }
            //??????Panel Layout Id

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
        System.out.println( "getContentContainerTop: " + result);
        return result;
    }

    private int getContentContainerHeight(int allHeight, int paddingTop, int scrollOutsideHeight) {
        int result;
        if (!contentScrollOutsizeEnable && !isResetState()) result = scrollOutsideHeight;
        else result = 0;
        return allHeight - paddingTop - result;
    }

    private boolean isBoundChange(int l, int t, int r, int b) {
        System.out.println("isBoundChange  is run  l"+l);
        System.out.println("isBoundChange  is run  t"+t);
        System.out.println("isBoundChange  is run  r"+r);
        System.out.println("isBoundChange  is run  b"+b);
        boolean change;
        if (realBounds == null || realBounds.left != l || realBounds.top != t || realBounds.right != r || realBounds.bottom != b) {
            change = true;
        } else {
            change = false;
        }
        realBounds = new Rect(l, t, r, b);
        System.out.println("isBoundChange  is  change --->"+realBounds);
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

    /**????????????Panel?????????
     * @param panelId
     * @return
     */
    public boolean checkoutPanel(int panelId) {
        System.out.println("get checkoutPanel panelId is run  "+panelId);
        System.out.println("get checkoutPanel doingCheckout  "+doingCheckout);
        if (doingCheckout) {
            return false;
        }
        System.out.println("get checkoutPanel doingCheckout  "+doingCheckout);
        doingCheckout = true;
        System.out.println("get checkoutPanel this.panelId  "+this.panelId);
        if (panelId == this.panelId) {
            doingCheckout = false;
            return false;
        }
        switch (panelId) {
            case Constants.PANEL_NONE:
                System.out.println("PANEL_NONE is run ?????????  ");
//                contentContainer.getInputActionImpl().hideKeyboard(true);
                contentContainer.getResetActionImpl().enableReset(false);
                break;
            case Constants.PANEL_KEYBOARD:
                System.out.println("PANEL_KEYBOARD is run ?????????  ");
                if (mCheckoutKeyboard) {
                    if (!contentContainer.getInputActionImpl().showKeyboard()) {
                        doingCheckout = false;
                        return false;
                    }
                }
                contentContainer.getResetActionImpl().enableReset(true);
                break;
            default:
                System.out.println("default is run ?????????  ");
                System.out.println("checkoutPanel --> getWidth() ,"+getWidth()+" getPaddingLeft() ,"+getPaddingLeft()+" getPaddingRight() ,"+getPaddingRight());
                System.out.println("getCompatPanelHeight(panelId)  ,"+getCompatPanelHeight(panelId));
                Pair size = new Pair(getWidth() - getPaddingLeft() - getPaddingRight(), getCompatPanelHeight(panelId));//????????????
                Pair oldSize = panelContainer.showPanel(panelId, size);
                System.out.println("----->"+oldSize.toString());
                if (size.f != oldSize.f || size.s != oldSize.s) {
                    notifyPanelSizeChange(panelContainer.getPanelView(panelId), DisplayUtil.getInstance().isPortrait(mContext), (int) oldSize.f, (int) oldSize.s, (int) size.f, (int) size.s);
                }
                contentContainer.getInputActionImpl().hideKeyboard(false);
                contentContainer.getResetActionImpl().enableReset(true);
                break;
        }

        this.lastPanelId = this.panelId;
        System.out.println("lastPanelId  ??? "+lastPanelId);
        this.panelId = panelId;
        //??????????????????
        postLayout();
        notifyPanelChange(this.panelId);
        doingCheckout = false;
        return true;
    }
    //??????Panel
    public  boolean HidePanelView(){
        return panelContainer.HidePanelView();
    }

    public boolean checkoutPanel(int panelId, boolean checkoutKeyboard) {
        System.out.println("checkoutPanel ????????? panelId ??? "+panelId);
        mCheckoutKeyboard = checkoutKeyboard;
        System.out.println( "checkoutPanel: panelId and  checkoutKeyboard  is run ");
        if (doingCheckout) {
            System.out.println( "checkoutPanel: is checkouting,just ignore!");
            return false;
        }
        doingCheckout = true;
        if (panelId == this.panelId) {
            System.out.println( "checkoutPanel: current panelId is " + panelId + ",just ignore!");
            doingCheckout = false;
            return false;
        }
        switch (panelId) {
            case Constants.PANEL_NONE:
                //????????????
                System.out.println( "PANEL_NONE   is run");
                DisplayUtil.hideSoftInput();
                contentContainer.getInputActionImpl().hideKeyboard(true);
                contentContainer.getResetActionImpl().enableReset(false);
                break;
            case Constants.PANEL_KEYBOARD:
                if (mCheckoutKeyboard) {
                    if (!contentContainer.getInputActionImpl().showKeyboard()) {
                        System.out.println( "checkoutPanel: system show keyboard fail, just ignore! ");
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
                //????????????
                DisplayUtil.hideSoftInput();
//                contentContainer.getInputActionImpl().hideKeyboard(false);
//                contentContainer.getResetActionImpl().enableReset(true);
                break;
        }
        System.out.println("lastPanelId  ---- "+lastPanelId);
        System.out.println("panelId ------"+this.panelId);
        this.lastPanelId = this.panelId;

        this.panelId = panelId;
        System.out.println( "checkoutPanel: checkout success ! ");
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
        System.out.println( "isResetState:   panelId  ??? " + panelId);
        return isResetState(panelId);
    }

    private boolean isKeyboardState(int panelId) {
        System.out.println("isKeyboardState  ??????ID  "+panelId);
        return panelId == Constants.PANEL_KEYBOARD ? true : false;
    }

    private boolean isPanelState(int panelId) {
        return !isResetState(panelId) && !isKeyboardState(panelId);
    }

    private boolean isResetState(int panelId) {
        System.out.println( "isResetState: panelId  " + panelId);
        return panelId == Constants.PANEL_NONE ? true : false;
    }

    private int getCompatPanelHeight(int panelId) {
        System.out.println("getCompatPanelHeight:  is run ");
        System.out.println("getCompatPanelHeight  panelId  "+panelId);
        System.out.println("getCompatPanelHeight  isPanelState(panelId)  "+isPanelState(panelId));
        if (isPanelState(panelId)) {
            PanelHeightMeasurer panelHeightMeasurer = (PanelHeightMeasurer) panelHeightMeasurers.get(panelId);
            if (panelHeightMeasurer != null) {
                System.out.println("panelHeightMeasurer : "+panelHeightMeasurer);
                //???????????????????????????????????????????????????????????????????????????????????? v hasMeasuredKeyboard(getContext()
                if (!PanelUtil.getInstance().hasMeasuredKeyboard(mContext) || !panelHeightMeasurer.synchronizeKeyboardHeight()) {
                    int result = panelHeightMeasurer.getTargetPanelDefaultHeight();
                    System.out.println( "getCompatPanelHeight: getCompatPanelHeight by default panel " + result);
                    return result;
                }

            }

        }
        int result = PanelUtil.getInstance().getKeyBoardHeight(mContext);
        System.out.println( "getCompatPanelHeight: " + result);
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
        System.out.println("notifyPanelChange is run ");
        if (panelChangeListeners != null) {
            System.out.println("panelChangeListeners  not null");
            for (OnPanelChangeListener listener : panelChangeListeners) {
                switch (panelId) {
                    case Constants.PANEL_NONE:
                        System.out.println("notifyPanelChange  PANEL_NONE  is run");
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
        System.out.println("hookSystemBackByPanelSwitcher  is run ");
        System.out.println("!isResetState()   "+!isResetState());
        if (!isResetState()) {
            //?????????????????????????????????????????????  checkoutPanel(Constants.PANEL_NONE)?????????????????????????????? recyclerview ?????? layout ?????????????????????????????????
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
            System.out.println("result "+result);
            if (!result && panelId != Constants.PANEL_KEYBOARD && retry) {
                eventHandler = new EventHandler(EventRunner.getMainEventRunner());
                eventHandler.postTask(this, delay);
                System.out.println( "" + eventHandler.isIdle());
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
