package com.borf.bookman.manager;

import com.borf.bookman.base.BaseFragment;
import com.borf.bookman.fragment.QDButtonFragment;
import com.borf.bookman.model.QDItemDescription;

import java.util.HashMap;
import java.util.Map;

class QDWidgetContainer {
  private static QDWidgetContainer sInstance = new QDWidgetContainer();

  private Map<Class<? extends BaseFragment>, QDItemDescription> mWidgets;

  private QDWidgetContainer() {
    mWidgets = new HashMap<>();
    mWidgets.put(QDButtonFragment.class, new QDItemDescription(QDButtonFragment.class, "QMUIRoundButton", 2131492876, ""));
     }

  public static QDWidgetContainer getInstance() {
    return sInstance;
  }

  public QDItemDescription get(Class<? extends BaseFragment> fragment) {
    return mWidgets.get(fragment);
  }
}
