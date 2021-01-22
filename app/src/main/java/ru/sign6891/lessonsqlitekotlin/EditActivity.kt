package ru.sign6891.lessonsqlitekotlin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.edit_activity.*
import android.view.View
import android.webkit.RenderProcessGoneDetail
import android.widget.Toast
import ru.sign6891.lessonsqlitekotlin.db.MyDbManager
import ru.sign6891.lessonsqlitekotlin.db.MyIntentConstants


class EditActivity : AppCompatActivity() {

    val imageRequestCode = 11
    var tempImageUri = "empty"
    private val manager = MyDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)

        getMyIntents()
    }

    fun onClickSave(view: View) {
        val myTitle = edTitle.text.toString()
        val myDesc = edDescription.text.toString()

        if (myTitle != "" && myDesc != "") {
            manager.insertToDb(myTitle, myDesc, tempImageUri)
            Toast.makeText(this, "Заметка сохранена", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickAddImage(view: View) {
        mainImageLayoit.visibility = View.VISIBLE
        fbAddImage.visibility = View.GONE
    }

    fun onClickDeleteImage(view: View) {
        mainImageLayoit.visibility = View.GONE
        fbAddImage.visibility = View.VISIBLE
    }

    fun onClickChooseImage(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"

        startActivityForResult(intent, imageRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode) {
            imMainImage.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
            contentResolver.takePersistableUriPermission(
                data?.data!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }

    fun getMyIntents() {
        val i = intent
        if (i != null) {

            if (i.getStringExtra(MyIntentConstants.INTENT_TITLE_KEY) != null)
                edTitle.setText(i.getStringExtra(MyIntentConstants.INTENT_TITLE_KEY))
            if (i.getStringExtra(MyIntentConstants.INTENT_DESC_KEY) != null)
                edDescription.setText(i.getStringExtra(MyIntentConstants.INTENT_DESC_KEY))
            if (i.getStringExtra(MyIntentConstants.INTENT_URI_KEY) != null) {
                mainImageLayoit.visibility = View.VISIBLE
                fbAddImage.visibility = View.GONE
                imButtonEditImage.visibility = View.GONE
                imButtonDeleteImage.visibility = View.GONE
                imMainImage.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.INTENT_URI_KEY)))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        manager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.closeDb()
    }
}