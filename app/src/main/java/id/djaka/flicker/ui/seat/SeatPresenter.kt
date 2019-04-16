package id.djaka.flicker.ui.seat

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import id.djaka.flicker.base.BasePresenter
import id.djaka.flicker.injection.network.ApiServices
import id.djaka.flicker.model.User
import id.djaka.flicker.ui.main.MainActivity
import id.djaka.flicker.util.SharedKey
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

class SeatPresenter(loginView: SeatView) : BasePresenter<SeatView>(loginView){
    @Inject
    lateinit  var apiServices:ApiServices

}