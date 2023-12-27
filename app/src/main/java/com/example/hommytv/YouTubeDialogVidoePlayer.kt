package com.example.hommytv


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.hommytv.databinding.FragmentYouTubeDialogVidoePlayerBinding
import com.example.hommytv.networkrequest.RetrofitOjectForYouTube
import com.example.hommytv.retrofitdataclasses.TrailerVideo
import com.example.hommytv.retrofitdataclasses.YouTubeSearchResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class YouTubeDialogVidoePlayer : DialogFragment() {


    lateinit var views:FragmentYouTubeDialogVidoePlayerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        views=FragmentYouTubeDialogVidoePlayerBinding.inflate(inflater,container, false)
        return views.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)







//        making request for video




     val request=   RetrofitOjectForYouTube.networkRequestMethodsForYouTube.videoId(searchKeyword = "${arguments?.getString("KeywordForSearch")}")

      request.enqueue(object :Callback<YouTubeSearchResults>{
            override fun onResponse(
                call: Call<YouTubeSearchResults>,
                response: Response<YouTubeSearchResults>
            ) {


                if(response.isSuccessful){




//                    getting video(video html tag ) with id
                    RetrofitOjectForYouTube.networkRequestMethodsForYouTube.video(id = response.body()!!.items[0].id.videoId).enqueue(

                        object :Callback<TrailerVideo>{
                            override fun onResponse(
                                call: Call<TrailerVideo>,
                                responseX: Response<TrailerVideo>
                            ) {


                                if(responseX.isSuccessful){
//                                    Toast.makeText(requireActivity(),"id found",Toast.LENGTH_SHORT).show()
                                    dialog?.setCancelable(false)
//                                    converting video srtring's escape characters into < and >
                                    var videoHtmlTag= responseX.body()!!.items[0].player.embedHtml


                                    videoHtmlTag=videoHtmlTag.replace("480","100%")
                                    videoHtmlTag=videoHtmlTag.replace("270","100%")
                                    videoHtmlTag=videoHtmlTag.replace("//www.youtube.com/embed/${response.body()!!.items[0].id.videoId}","https://www.youtube.com/embed/${response.body()!!.items[0].id.videoId}")
                                    videoHtmlTag=   videoHtmlTag.replace("web-share","")


//                                    making the necessar views visible
                                    views.determinateBar.visibility=View.GONE
                                    views.webView.visibility=View.VISIBLE
                                    views.cancelButton.visibility=View.VISIBLE
//                              setting video

                                    views.webView.loadData(videoHtmlTag,"text/html","utf-8")
                                    views.webView.settings.javaScriptEnabled=true


                                }


                                else{

                                    Toast.makeText(requireActivity(),"${responseX.code()}",Toast.LENGTH_SHORT).show()
                                    dialog?.cancel()
                                }
                            }

                            override fun onFailure(call: Call<TrailerVideo>, t: Throwable) {
                                Toast.makeText(requireActivity(),"Network Error",Toast.LENGTH_SHORT).show()
                                dialog?.cancel()

                            }


                        }
                    )


                }
                else{

                    Toast.makeText(requireActivity(),"${response.code()}",Toast.LENGTH_SHORT).show()
                    dialog?.cancel()
                }


            }

            override fun onFailure(call: Call<YouTubeSearchResults>, t: Throwable) {

                Toast.makeText(requireActivity(),"Network Error",Toast.LENGTH_SHORT).show()
                dialog?.cancel()

            }


        })


        views.cancelButton.setOnClickListener {
            dialog?.cancel()
        }



    }



}