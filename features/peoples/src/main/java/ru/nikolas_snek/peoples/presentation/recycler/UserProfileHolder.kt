package ru.nikolas_snek.peoples.presentation.recycler

import android.graphics.Bitmap
import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import ru.nikolas_snek.data.utils.UserStatus
import ru.nikolas_snek.peoples.databinding.ProfileItemBinding
import ru.nikolas_snek.peoples.domain.Profile

class UserProfileHolder(val binding: ProfileItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private val transformation = object : Transformation {
        override fun transform(source: Bitmap): Bitmap {
            val width = source.width
            val height = source.height
            val size = minOf(width, height)
            val result =
                Bitmap.createBitmap(source, (width - size) / 2, (height - size) / 2, size, size)
            if (result != source) {
                source.recycle()
            }
            return result
        }

        override fun key(): String {
            return "square()"
        }
    }

    fun bind(profile: Profile) {
        with(binding) {
            tvRecyclerUserEmail.text = profile.email
            tvRecyclerUserName.text = profile.name
            if (profile.photoUrl != "") {
                Picasso.get().load(profile.photoUrl).transform(transformation)
                    .into(ivRecyclerProfilePhoto)
            }
            val statusColor = when (profile.onlineStatus) {
                UserStatus.OFFLINE -> ContextCompat.getColor(
                    binding.root.context,
                    ru.nikolas_snek.ui_kit.R.color.offline
                )

                UserStatus.ACTIVE -> ContextCompat.getColor(
                    binding.root.context,
                    ru.nikolas_snek.ui_kit.R.color.online
                )

                UserStatus.IDLE -> ContextCompat.getColor(
                    binding.root.context,
                    ru.nikolas_snek.ui_kit.R.color.idle
                )

            }
            onlineStatus.setColorFilter(statusColor, PorterDuff.Mode.SRC_IN)
        }

    }
}