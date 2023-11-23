package com.example.hommytv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.hommytv.databinding.FragmentLogInBinding


class LogInFragment : Fragment() {


    lateinit var views:FragmentLogInBinding
    val viewModel:TheViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        views=FragmentLogInBinding.inflate(inflater,container,false)
        return views.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.context=requireActivity()

        views.loginButton.setOnClickListener {

//            check if both edit text is not empty and if account exist
            checkingEmptyTextFields()




        }



        views.createAccountButton.setOnClickListener {

            val fragTransaction= parentFragmentManager.beginTransaction()

            fragTransaction.replace(R.id.frag_layout_main_activity,SignUp())

            fragTransaction.addToBackStack(null)
            fragTransaction.commit()


        }





    }

    //        function for checking empty  text fields
    fun checkingEmptyTextFields(){

        if(views.loginEmail.text.toString()!="" && views.loginPassword.text.toString()!=""){
//                checking if account exist in online database
            checkingAccountOnFirebase()


        }

        else{
            Toast.makeText(requireActivity(),"All Fields Must Be Filled To Login",Toast.LENGTH_SHORT).show()
        }


    }




    //        funtion for checking the existence of the account on firebase
    fun checkingAccountOnFirebase(){

//            check for the account on firebase(Not yet implemented) and if it exist make
//            network request to server and then make the fragment
//            pops off the stack


        //            logInto account method is what will perform the log in by checking if the account exist
        viewModel.logIntoAcount(views.loginEmail.text.toString(),views.loginPassword.text.toString())


        //      start of network request to server for the home tab
        viewModel.gettingAllDataForHomeTabFromServer()

        parentFragmentManager.popBackStack()



    }




}