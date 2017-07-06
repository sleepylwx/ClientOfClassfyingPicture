package com.lwx.user.view;

import android.support.design.widget.NavigationView;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.Button;

import com.lwx.user.BuildConfig;
import com.lwx.user.R;
import com.lwx.user.ui.activity.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.*;
/**
 * Created by 36249 on 2017/7/5.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    MainActivity mainActivity;
    RecyclerView recyclerView;
    NavigationView navigationView;
    Menu menu;
    @Before
    public void setUp(){

        mainActivity = Robolectric.setupActivity(MainActivity.class);
        recyclerView = (RecyclerView) mainActivity.findViewById(R.id.recyclerview);
        navigationView = (NavigationView) mainActivity.findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
    }
    @Test
    public void mainActivityTest() throws Exception{


        assertNotNull(mainActivity);
        assertEquals(mainActivity.getTitle(),"User");
        //assertEquals(mainActivity.getTitle(),"MainActivity");
    }

    @Test
    public void recyclerviewTest() throws Exception{



        assertNotNull(recyclerView);
    }

    @Test
    public void navigationViewTest() throws Exception{

        assertNotNull(navigationView);
    }

    @Test
    public void navigationViewMenuTest()throws Exception{

        assertNotNull(menu);
    }

    @Test
    public void navigationViewMenuItemTest()throws Exception{

        for(int i = 0; i < menu.size(); ++i){

            assertNotNull(menu.getItem(i));
        }
    }



}
