package com.testprep.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testprep.R
import com.testprep.adapter.TutorReviewAdapter
import kotlinx.android.synthetic.main.fragment_tutors_review.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TutorsReviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutors_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        tutor_review_rvReview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//
        tutor_review_rvReview.adapter = TutorReviewAdapter(activity!!)

    }

}
