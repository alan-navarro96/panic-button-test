package tpi.panicbutton


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import android.content.Intent
import android.widget.TextView
import android.R.attr.data
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.twitter.sdk.android.core.TwitterAuthToken
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.services.StatusesService
import kotlinx.android.synthetic.main.fragment_settings.*
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.models.Tweet
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.content.Context.INPUT_METHOD_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SettingsFragment : Fragment() {

    private lateinit var mView: View

    private  lateinit var loginButton: TwitterLoginButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_settings, container, false)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = recyclerview_phonenumbers as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

        val adapter = CustomAdapter(getNumbers())

        recyclerView.adapter = adapter

        add_number_button.setOnClickListener {
            var act = (activity as MainActivity)
            act.addSmsNumber(type_number_editText.text.toString())
            adapter.notifyDataSetChanged()
        }

        loginButton = login_button as TwitterLoginButton
        loginButton.setCallback(object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                val session = TwitterCore.getInstance().sessionManager.activeSession
                val authToken = session.authToken

                val userName = result.data.userName
                val userID = result.data.id

                val token = authToken.token
                val secret = authToken.secret

                Log.e("worked", "showing toast")
                Toast.makeText(context!!, "Exitosa conexion a Twitter", Toast.LENGTH_SHORT).show()
            }

            override fun failure(exception: TwitterException) {
                Toast.makeText(context!!, "Fallada conexion a Twitter", Toast.LENGTH_SHORT).show()
            }
        })
    }

//    fun updateListOfPhones() {
//        list_numbers.text = getNumbersAsText()
//    }

//    fun getNumbersAsText(): String {
//        var act = (activity as MainActivity)
//        var numbers = act.getSmsNumbers()
//        return numbers.joinToString("\n")
//    }

    fun getNumbers(): ArrayList<PhoneNumber> {
        var act = (activity as MainActivity)
        return act.getSmsNumbers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Pass the activity result to the login button.

        Log.e("worked", "sending to button")
        loginButton.onActivityResult(requestCode, resultCode, data)
    }


}
