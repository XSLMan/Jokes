package com.along.project.jokes.ui.main;

import com.along.project.jokes.base.BaseModel;
import com.along.project.jokes.base.BasePresent;
import com.along.project.jokes.base.BaseView;

/**
 * Created by 16419 on 2018/5/10.
 */

public interface MainContract {
    interface View extends BaseView<Present>{
    }

    abstract class Present<T> extends BasePresent<View>{
    }

    interface Model extends BaseModel{
    }
}
