package com.datarockets.mnchkn.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import butterknife.*
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.ui.base.BaseActivity

class NewPlayerDialogFragment : BottomSheetDialogFragment() {

    @BindView(R.id.et_player_name) lateinit var etPlayerName: EditText
    @BindView(R.id.btn_add_new_player) lateinit var btnAddNewPlayer: Button

    private lateinit var mListener: NewPlayerDialogListener
    private lateinit var mUnbinder: Unbinder

    interface NewPlayerDialogListener {
        fun onFinishEditDialog(inputName: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = activity as NewPlayerDialogListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).activityComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, bundle: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_add_player, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUnbinder = ButterKnife.bind(this, view!!)
    }

    @OnClick(R.id.btn_add_new_player)
    fun onAddNewPlayerClick() {
        passNameToActivity()
    }

    @OnEditorAction(R.id.et_player_name)
    fun onEditorAction(actionId: Int): Boolean {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            passNameToActivity()
            return true
        }
        return false
    }

    private fun passNameToActivity() {
        val name = etPlayerName.text.toString()
        if (!name.isEmpty()) {
            mListener.onFinishEditDialog(name)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder.unbind()
    }

}
