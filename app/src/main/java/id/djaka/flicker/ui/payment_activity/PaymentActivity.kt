package id.djaka.flicker.ui.payment_activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import id.djaka.flicker.R
import id.djaka.flicker.base.BaseActivity
import id.djaka.flicker.model.Reservation
import id.djaka.flicker.util.RESERVATION
import id.djaka.flicker.util.Utill
import kotlinx.android.synthetic.main.activity_payment.*
import okhttp3.MediaType
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class PaymentActivity : BaseActivity<PaymentPresenter>(), PaymentView {
    var oldUrl = ""
    override fun showImage(body: Reservation) {
        Log.d("TAG", if(body.paymentProof == null) "true" else "false")
        if(body.paymentProof != null) Glide.with(this).load(body.paymentProof).into(img_upload)
        else img_upload.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.upload))

        if(body.paymentProof != null && oldUrl == body.paymentProof && oldUrl != "")
            Toast.makeText(this, "Kesempatan revisi sudah habis, silahkan pesan ulang tiket anda", Toast.LENGTH_LONG).show()
        oldUrl = if(body.paymentProof != null) body.paymentProof!! else ""
    }

    private var uri: Uri? = null

    override fun instantiatePresenter(): PaymentPresenter {
        return PaymentPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        prepareBtn()
        showImage(intent.getParcelableExtra(RESERVATION.toString()))
    }

    private fun prepareBtn() {
        fl_payment_ib.setOnClickListener {
            if(el_payment_ib.isExpanded)
                el_payment_ib.collapse()
            else
                el_payment_ib.expand()
        }
        img_upload.setOnClickListener { presenter.uploadImg(this) }
        btn_seat.setOnClickListener { finish() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PaymentPresenter.REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            uri = data!!.data
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show()
                val id = RequestBody.create(MediaType.parse("text/plain"), intent.getParcelableExtra<Reservation>(
                    RESERVATION.toString()).id.toString())
               presenter.doUpload(id, Utill.createImgPart(
                    File(Utill.getRealPathFromURIPath(uri!!, this))
               )
                )
            } else {
                EasyPermissions.requestPermissions(this, "Required Permission",
                    PaymentPresenter.READ_REQUEST_CODE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }
}
