package com.lwx.user.presenter;

import com.lwx.user.BuildConfig;
import com.lwx.user.ui.activity.HistoryImageActivity;
import com.lwx.user.ui.activity.ImageDetailActivity;
import com.lwx.user.ui.activity.LoginActivity;
import com.lwx.user.ui.activity.MainActivity;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.*;
/**
 * Created by 36249 on 2017/7/7.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PresenterTest {


    private MainPresenter mainPresenter;
    private LoginPresenter loginPresenter;
    private ImageDetailPresenter imageDetailPresenter;
    private HistoryImagePresenter historyImagePresenter;

    @Before
    public void setUp(){

        mainPresenter = new MainPresenter(Robolectric.setupActivity(MainActivity.class));
        loginPresenter = new LoginPresenter(Robolectric.setupActivity(LoginActivity.class));
        imageDetailPresenter = new ImageDetailPresenter(Robolectric.setupActivity(ImageDetailActivity.class),false);
        historyImagePresenter = new HistoryImagePresenter(Robolectric.setupActivity(HistoryImageActivity.class));

    }
    @Test
    public void mainPresenterTest(){

        assertNotNull(mainPresenter);

    }

    @Test
    public void loginPresenterTest(){

        assertNotNull(loginPresenter);
    }

    @Test
    public void imageDetailPresenterTest(){

        assertNotNull(imageDetailPresenter);
    }

    @Test
    public void historyImagePresenterTest(){

        assertNotNull(historyImagePresenter);
    }
}
