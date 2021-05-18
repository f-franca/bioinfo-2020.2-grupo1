package bionfo.galaxyapp.ui.login

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson


class RequestHandler: AppCompatActivity() {
    data class LoginResponseJsonObj(
        var api_key: String = "") {
    }

    private fun loginRequest(email: String,pass: String, appContext: Context): String {
        val url = "http://galaxy.biowebdb.org/api/authenticate/baseauth"
        val queue = Volley.newRequestQueue(appContext)

//        var requestSuccess: Boolean = false
        var userApiKeyObj: LoginResponseJsonObj
        var userApiKeyStr: String = ""

        val stringRequest = object: StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                userApiKeyObj = Gson().fromJson(response, LoginResponseJsonObj::class.java)
//                requestSuccess = true
                userApiKeyStr = userApiKeyObj.api_key
                Log.d("LoginRequest", "Login Response is: $userApiKeyStr"); //.substring(0,500)
            },
            Response.ErrorListener {
                Log.d("LoginRequest", "LOGIN REQUEST FAILED: $it"); //.substring(0,500))
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val oriString = "$email:$pass"
                val encodedAuth: String = Base64.encodeToString(oriString.toByteArray(Charsets.UTF_8),Base64.DEFAULT)
//                Log.d("LoginRequest", "Params: [$email]:[$pass] | String: [$oriString] | Encoded Auth: $encodedAuth")
                headers["Authorization"] = "Basic $encodedAuth" //"Basic <<YOUR BASE64 USER:PASS>>"
                return headers
            }
        }

        queue.add(stringRequest)

        return userApiKeyStr
    }

    public fun historyRequest(email: String,pass: String, appContext: Context){
        val apiKey: String = loginRequest(email,pass,appContext)

        Log.d("LoginRequest", "PRE HISTORY: [$apiKey]"); //.substring(0,500)
        val url = "http://galaxy.biowebdb.org/api/histories/?key=$apiKey"
//        val url = "http://galaxy.biowebdb.org/api/histories/?key=e80a649a4da1136478edc274fb4dbb8c"
        val queue = Volley.newRequestQueue(appContext)

        var requestSuccess: Boolean = false
        var userApiKeyObj: LoginResponseJsonObj

        val stringRequest = object: StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
//                userApiKeyObj = Gson().fromJson(response, LoginResponseJsonObj::class.java)
                requestSuccess = true
                Log.d("LoginRequest", "History Response is: $response"); //.substring(0,500)
            },
            Response.ErrorListener {
                Log.d("LoginRequest", "HISTORY REQUEST FAILED: $it"); //.substring(0,500))
            })
        {}

        queue.add(stringRequest)

    }
}