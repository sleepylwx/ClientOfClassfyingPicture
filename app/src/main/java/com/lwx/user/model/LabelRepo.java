package com.lwx.user.model;

import com.lwx.user.model.model.Label;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by henry on 17-4-19.
 */

public interface LabelRepo {
    Completable saveLabel(Label label);
    Completable deleteLabel(Label label);
    Observable<List<Label>> getAllLabels();
    Observable<Label> getLabel(String label);
}
