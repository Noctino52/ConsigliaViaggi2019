package ingsw.mobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import ingsw.mobile.databinding.RecensioneRowBinding
import ingsw.mobile.entity.Review
import ingsw.mobile.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Calendar
import java.util.Locale

class RecAdapter(context: Context?, textViewResourceId: Int, objects: List<Review?>?):
    ArrayAdapter<Review?>(context!!, textViewResourceId, objects!!) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = creaORiciclaBinding(convertView, parent)
        val review: Review = getItem(position)!!

        binding.apply {
            textViewNomeUtenteRecensione.text = ottieniPreferenzaNome(review)
            textViewDescrizioneRecensione.text = review.testo
            textViewDataScritturaRecensione.text = ottieniDataFormattata(review.dataCreazione)
        }
        impostaStelle(review.valutazione.toFloat(), binding)

        return binding.root
    }

    private fun creaORiciclaBinding(convertView: View?, parent: ViewGroup): RecensioneRowBinding {
        return if(convertView != null) {
            DataBindingUtil.getBinding(convertView)!!
        }
        else {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                    as LayoutInflater
            DataBindingUtil.inflate(inflater,
                R.layout.recensione_row, parent, false)
        }
    }

    private fun ottieniDataFormattata(date: Date): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date.time
        return formatter.format(calendar.time)
    }

    private fun ottieniPreferenzaNome(review: Review): String {
        return if(review.autore.preferisceNomeReale!!) {
            "${review.autore.nomeReale} ${review.autore.cognomeReale}"
        }
        else {
            "@${review.autore.nomeUtente}"
        }
    }

    private fun impostaStelle(valutazione: Float, binding: RecensioneRowBinding) {
        binding.apply {
            imageViewStella1Recensione.setImageResource(ottieniStella( 1f, valutazione))
            imageViewStella2Recensione.setImageResource(ottieniStella( 2f, valutazione))
            imageViewStella3Recensione.setImageResource(ottieniStella( 3f, valutazione))
            imageViewStella4Recensione.setImageResource(ottieniStella( 4f, valutazione))
            imageViewStella5Recensione.setImageResource(ottieniStella( 5f, valutazione))
        }
    }

    private fun ottieniStella(numeroStella: Float, valutazione: Float): Int {
        return if(numeroStella <= valutazione) {
            R.drawable.ic_star_black_24dp
        }
        else {
            R.drawable.ic_star_border_black_24dp
        }
    }
}