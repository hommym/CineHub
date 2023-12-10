package com.example.hommytv

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.hommytv.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var views:ActivityMainBinding
    val viewModel:TheViewModel by viewModels()
     lateinit var applicationInstance:App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views=ActivityMainBinding.inflate(layoutInflater)
        setContentView(views.root)

//passing the the value for the context variable in the viewmodel
viewModel.context=this@MainActivity

//initialising application instance
applicationInstance=application as App











            lifecycleScope.launch(Dispatchers.IO) {

                App.objectOFLogInSessionDataStore.readData.collect{

    it[LoginSesionDataStore.key]?.let{data->
//                    checking if user has already log in and if is true code below gets executed
            if (data){

//                making the views in the activity visible

//             the purpose of this condition is to make the buttom nav bar visible as long as the
//            the current fragment in the layout for section fragment
//            is not profile fragment(this is for when the activity restarts)
                            if(viewModel.currentFragmentSectionsLayout !is ProfileFragment){
                                views.bottomNavigation.visibility=View.VISIBLE
                            }

//               starting network request if no network request has being made yet
//               (this is for when the activity restarts)
                if(viewModel.returnedRecentMovies==null){
                //      start of network request to server for the home tab
                viewModel.gettingAllDataForHomeTabFromServer()


                    //condition for checking id the layout for the home fragment is empty before insertion becuase it
                    // might not be if the activity restarts
                    if(viewModel.currentFragmentSectionsLayout==null){
                        fragmentInsertion(HomeFragment(), layoutForInsertion = R.id.layout_for_sections, addToBackStack = true)
                    }



                            }

                        }
            else{
//                   inserting logIn fragment if lodInSessionDataStore value is falsee or null
                    fragmentInsertion(LogInFragment(),true)
                }
    }?: fragmentInsertion(LogInFragment(),true)




                }

            }





//        adding click listner to buttom nav bar
        views.bottomNavigation.setOnItemSelectedListener {

            if(it.itemId==R.id.home_section){
                if(supportFragmentManager.findFragmentById(R.id.layout_for_sections) !is HomeFragment){
                    fragmentInsertion(HomeFragment(), layoutForInsertion = R.id.layout_for_sections)
                    true
                }
                else{
                    false
                }

            }

            else if(it.itemId==R.id.category_section){
                if(supportFragmentManager.findFragmentById(R.id.layout_for_sections) !is CategoryFragment){
                    fragmentInsertion(CategoryFragment(),layoutForInsertion = R.id.layout_for_sections)
                    true
                }
                else{
                    false
                }
            }

            else if(it.itemId==R.id.search_section){

                if(supportFragmentManager.findFragmentById(R.id.layout_for_sections) !is SearchFragment){
                    fragmentInsertion(SearchFragment(),layoutForInsertion = R.id.layout_for_sections)
                    true
                }
                else{
                    false
                }

            }

            else{
                if(supportFragmentManager.findFragmentById(R.id.layout_for_sections) !is DownloadFragment){
                    fragmentInsertion(DownloadFragment(),layoutForInsertion = R.id.layout_for_sections)
                    true
                }
                else{
                    false
                }
            }

        }


        viewModel.hasLoggedIn.observe(this@MainActivity, Observer {


            if (it){
//                making the views in the activity visible


//             the purpose of this condition is to make the buttom nav bar visible as long as the
//            the current fragment in the layout for section fragment is not profile fragment
                if(viewModel.currentFragmentSectionsLayout !is ProfileFragment){
                    views.bottomNavigation.visibility=View.VISIBLE
                }



            }

        })










// making buttom nav bar dissappear if current fragment is profile

       lifecycleScope.launch {
           viewModel.isCurrentFragmentProfile.collect{

                if (it ){
                    views.bottomNavigation.visibility=View.INVISIBLE
                }
               else{
                    views.bottomNavigation.visibility=View.VISIBLE
                }


           }
       }










    }






//    the fragment insertion function is used for inserting fragments into this activity
    fun fragmentInsertion(fragmentObject:Fragment,addToBackStack:Boolean=false,
      layoutForInsertion:Int=R.id.frag_layout_main_activity){

        val fm= supportFragmentManager
        val ft=fm.beginTransaction()

    ft.setCustomAnimations(R.anim.slide_in,R.anim.fade_out,R.anim.fade_in,R.anim.slide_out)

    if (supportFragmentManager.findFragmentById(R.id.layout_for_sections)!=null){

        ft.replace(layoutForInsertion,fragmentObject)
    }

    else{
        ft.add(layoutForInsertion,fragmentObject)
    }


        if (addToBackStack){
            ft.addToBackStack(null)
        }




        ft.commit()



    }





}