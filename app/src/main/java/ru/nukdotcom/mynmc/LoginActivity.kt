package ru.nukdotcom.mynmc

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.nukdotcom.mynmc.databinding.ActivityLoginBinding
import ru.nukdotcom.mynmc.databinding.ActivityMainBinding
import ru.nukdotcom.mynmc.helpers.AuthHandler
import ru.nukdotcom.mynmc.helpers.MyNMCWebAPI
import ru.nukdotcom.mynmc.helpers.RetrofitBuilder
import ru.nukdotcom.mynmc.models.me.Me
import ru.nukdotcom.mynmc.models.token_response.TokenResponse

class LoginActivity : AppCompatActivity() {

    companion object{
        public val RESULT_OK = 1
        public val RESULT_FAIL = 2
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLoginBinding

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val showPasswordSwitch = findViewById<Switch>(R.id.showPasswordSwitch)
        val password = findViewById<EditText>(R.id.password)
        val email = findViewById<EditText>(R.id.email)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val authHandler = AuthHandler(this)


        showPasswordSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) password.transformationMethod = SingleLineTransformationMethod()
            else password.transformationMethod = PasswordTransformationMethod()
        }

        val retrofit = RetrofitBuilder.buildDefaultRetrofit(this)

        loginButton.setOnClickListener {


            val api = retrofit.create(MyNMCWebAPI::class.java)
            api.login(email.text.toString(), password.text.toString()).enqueue(object: Callback<TokenResponse>{
                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    if (response.isSuccessful){
                        if (response.body() == null || response.body()!!.accessToken == null){
                            Log.e("LoginActivity", response.raw()!!.toString())
                            Toast.makeText(
                                this@LoginActivity,
                                R.string.error_unexpected_response,
                                Toast.LENGTH_LONG)
                                .show()
                            return
                        }
                        //So everything's okay, retrieving info

                        val token = response.body()!!.accessToken

                        api.me("Bearer $token").enqueue(object: Callback<Me>{
                            override fun onResponse(call: Call<Me>, response: Response<Me>) {
                                if (response.isSuccessful){
                                    if (response.body() == null ||
                                        response.body()!!.id == null ||
                                            response.body()!!.name == null ||
                                            response.body()!!.role == null){
                                        Log.e("LoginActivity", response.raw()!!.toString())
                                        Toast.makeText(
                                            this@LoginActivity,
                                            R.string.error_unexpected_response,
                                            Toast.LENGTH_LONG)
                                            .show()
                                        return
                                    }
                                    authHandler.token = token!!
                                    authHandler.id = response.body()!!.id
                                    authHandler.email = response.body()!!.email
                                    authHandler.avatar = response.body()!!.avatar
                                    authHandler.name = response.body()!!.name
                                    authHandler.role = response.body()!!.role
                                    authHandler.group = response.body()!!.group
                                    authHandler.save()

                                    this@LoginActivity.setResult(RESULT_OK)
                                    this@LoginActivity.finish()
                                }
                                else{
                                    Log.e("LoginActivity", response.raw()!!.toString()!!)
                                    Toast.makeText(this@LoginActivity, String.format(getString(R.string.error_login_failed), response.code(), response.message()), Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun onFailure(call: Call<Me>, t: Throwable) {
                                Log.e("LoginActivity", t.message!!)
                                Toast.makeText(this@LoginActivity, R.string.error_request_failed, Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                    else {
                        Log.e("LoginActivity", response.raw()!!.toString()!!)
                        Toast.makeText(this@LoginActivity, String.format(getString(R.string.error_login_failed), response.code(), response.message()), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    Log.e("LoginActivity", t.message!!)
                    Toast.makeText(this@LoginActivity, R.string.error_request_failed, Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_FAIL)
        finish()
    }
}