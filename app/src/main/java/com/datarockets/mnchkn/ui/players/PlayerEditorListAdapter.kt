package com.datarockets.mnchkn.ui.players

import android.graphics.Color
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.amulyakhare.textdrawable.TextDrawable
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.ui.players.helpers.ItemTouchHelperAdapter
import com.datarockets.mnchkn.ui.players.helpers.ItemTouchHelperViewHolder
import java.util.*
import javax.inject.Inject

class PlayerEditorListAdapter
@Inject constructor() : RecyclerView.Adapter<PlayerEditorListAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    private val mPlayers = mutableListOf<Player>()

    init {
        setHasStableIds(true)
    }

    interface OnItemClickListener {
        fun onPlayerItemClick(playerId: Long)
    }

    interface OnItemCheckboxClickListener {
        fun onPlayerItemCheckboxClick(playerId: Long, isPlaying: Boolean)
    }

    interface OnItemMovedListener {
        fun onItemMoved(fromPosition: Int, toPosition: Int)
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    var mClickListener: OnItemClickListener? = null
    var mCheckboxClickListener: OnItemCheckboxClickListener? = null
    var mDragStartListener: OnStartDragListener? = null
    var mItemMovedListener: OnItemMovedListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mClickListener = listener
    }

    fun setOnItemCheckboxClickListener(listener: OnItemCheckboxClickListener) {
        mCheckboxClickListener = listener
    }

    fun setOnStartDragListener(listener: OnStartDragListener) {
        mDragStartListener = listener
    }

    fun setOnItemMovedListener(listener: OnItemMovedListener) {
        mItemMovedListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.player_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        val player = mPlayers[position]

        val color = Color.parseColor(player.color)
        val capitalizedPlayerFirstLetter = player.name!!.substring(0, 1).toUpperCase()
        val drawable = TextDrawable.builder()
                .buildRound(capitalizedPlayerFirstLetter, color)

        holder?.ivPlayerImage?.setImageDrawable(drawable)
        holder?.tvPlayerName?.text = player.name
        holder?.cbIsPlaying?.isChecked = player.playing


        holder?.itemView?.setOnTouchListener { view, event ->
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mDragStartListener?.onStartDrag(holder)
            }
            false
        }

    }

    fun addPlayer(player: Player) {
        mPlayers.add(mPlayers.count(), player)
        notifyDataSetChanged()
    }

    fun setPlayers(players: List<Player>) {
        mPlayers.addAll(players)
        notifyDataSetChanged()
    }

    fun deletePlayer(playerId: Long) {
        val position = getPositionForItemId(playerId)
        mPlayers.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updatePlayerName(playerId: Long, playerName: String) {
        val position = getPositionForItemId(playerId)
        mPlayers[position].name = playerName
        notifyItemChanged(position)
    }

    override fun getItemId(position: Int): Long {
        return mPlayers[position].id
    }

    override fun getItemCount(): Int {
        return mPlayers.size
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        mItemMovedListener?.onItemMoved(fromPosition, toPosition)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(mPlayers, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        mPlayers.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getPositionForItemId(id: Long): Int {
        val count = itemCount
        for (i in 0..count - 1) {
            if (id == getItemId(i)) {
                return i
            }
        }
        return RecyclerView.NO_POSITION
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {

        @BindView(R.id.iv_player_color) lateinit var ivPlayerImage: ImageView
        @BindView(R.id.tv_player_name) lateinit var tvPlayerName: TextView
        @BindView(R.id.cb_is_playing) lateinit var cbIsPlaying: CheckBox

        init {
            ButterKnife.bind(this, view)
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val playerId = getItemId(position)
                    mClickListener?.onPlayerItemClick(playerId)
                }
            }
            cbIsPlaying.setOnCheckedChangeListener { compoundButton, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val playerId = getItemId(position)
                    mPlayers[position].playing = isChecked
                    mCheckboxClickListener?.onPlayerItemCheckboxClick(playerId, isChecked)
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }

    }

}
