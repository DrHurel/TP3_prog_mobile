package fr.hureljeremy.gitea.tp3.view

    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.CheckBox
    import android.widget.TextView
    import com.google.android.material.button.MaterialButton
    import com.google.android.material.textfield.TextInputEditText
    import fr.hureljeremy.gitea.tp3.R

    class Register : Fragment() {

        private lateinit var registerButton: MaterialButton
        private  lateinit var name: TextInputEditText
        private  lateinit var login : TextInputEditText
        private lateinit var surname: TextInputEditText
        private lateinit var passwordInput: TextInputEditText
        private lateinit var birthDate: TextInputEditText
        private lateinit var checkBoxMusic : CheckBox
        private lateinit var checkBoxSport : CheckBox
        private lateinit var checkBoxReading : CheckBox
        private lateinit var phone: TextInputEditText
        private lateinit var email: TextInputEditText


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_subscription, container, false)

            initializeViews(view)
            setupClickListeners()

            return view
        }

        private fun initializeViews(view: View) {
            registerButton = view.findViewById(R.id.register_submit_btn)
            name = view.findViewById(R.id.register_name_input)
            login = view.findViewById(R.id.register_login_input)
            surname = view.findViewById(R.id.register_surname_input)
            passwordInput = view.findViewById(R.id.register_password_input)
            birthDate = view.findViewById(R.id.register_birthdate_input)
            checkBoxMusic = view.findViewById(R.id.checkBoxMusique)
            checkBoxSport = view.findViewById(R.id.checkBoxSport)
            checkBoxReading = view.findViewById(R.id.checkBoxLecture)
            phone = view.findViewById(R.id.register_phone_number_input)
            email = view.findViewById(R.id.register_email_input)

        }

        private fun setupClickListeners() {
            registerButton.setOnClickListener {
                handleRegistration()
            }


        }

        private fun handleRegistration() {
            val username = login.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Register successful
                //go to validation page



            } else {
                // Show error
                if (username.isEmpty()) login.error = "Username is required"
                if (password.isEmpty()) passwordInput.error = "Password is required"
                if (name.text.toString().isEmpty()) name.error = "Name is required"
                if (surname.text.toString().isEmpty()) surname.error = "Surname is required"
                if (birthDate.text.toString().isEmpty()) birthDate.error = "Birthdate is required"
                if (phone.text.toString().isEmpty()) phone.error = "Phone number is required"
                if (email.text.toString().isEmpty()) email.error = "Email is required"


            }
        }

        companion object {
            @JvmStatic
            fun newInstance() = Register()
        }
    }