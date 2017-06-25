package com.lwx.user.contracts;

import java.util.List;

/**
 * Created by 36249 on 2017/6/24.
 */

public class HistoryLabelContract {

    public interface View extends BaseContract.View<HistoryLabelContract.Presenter>{


        void onSignedLabelsLoadedSuccess(List<String> labels);
        void onSignedLabelsLoadedFailed();
    }

    public interface Presenter extends  BaseContract.Presenter<HistoryLabelContract.View>{

        void getSignedLabels(long uid);

    }
}
