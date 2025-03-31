package fr.hureljeremy.gitea.tp3.view

            import android.app.Dialog
            import android.os.Bundle
            import android.view.View
            import android.widget.EditText
            import android.widget.Toast
            import androidx.fragment.app.DialogFragment
            import com.google.android.material.dialog.MaterialAlertDialogBuilder
            import fr.hureljeremy.gitea.tp3.R

            interface PopUpFormListener {
                fun onTextSaved(slot: Int, text: String)
            }

            class PopUpForm(private val number: Int, private val text: String) : DialogFragment() {

                private var listener: PopUpFormListener? = null

                fun setListener(listener: Any) {
                    this.listener = listener as PopUpFormListener
                }

                override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                    val builder = MaterialAlertDialogBuilder(requireContext())
                    val inflater = requireActivity().layoutInflater
                    val view: View = inflater.inflate(R.layout.fragment_popup_form, null)

                    val textInput = view.findViewById<EditText>(R.id.editTextDescription)
                    textInput.setText(text)

                    builder.setView(view)
                        .setTitle("Slot $number")
                        .setPositiveButton("Save") { _, _ ->
                            val newText = textInput.text.toString()
                            listener?.onTextSaved(number, newText)
                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Cancel") { _, _ ->
                            dismiss()
                        }

                    return builder.create()
                }
            }