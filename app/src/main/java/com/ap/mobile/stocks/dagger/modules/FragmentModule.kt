package com.ap.mobile.stocks.dagger.modules

import com.ap.mobile.stocks.ui.detail.StockDetailDialog
import com.ap.mobile.stocks.ui.detail.StockDetailFragment
import com.ap.mobile.stocks.ui.main.MainActivityFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author Abhishek Prajapati
 * @version 1.0.0
 * @since 1/2/18.
 *
 * this class will inject all the fragments in our app
 */

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainActivityFragment

    @ContributesAndroidInjector
    abstract fun contributeStockDetailDialogFragment(): StockDetailDialog

    @ContributesAndroidInjector
    abstract fun contributeStockDetailFragment(): StockDetailFragment
}