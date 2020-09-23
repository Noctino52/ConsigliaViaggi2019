package ingsw.mobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import ingsw.mobile.databinding.StruttureRowBinding
import ingsw.mobile.entity.Structure
import ingsw.mobile.R

class StructAdapter(context: Context?,
                    textViewResourceId: Int, objects: List<Structure>?) :
    ArrayAdapter<Structure>(context!!, textViewResourceId, objects!!) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = creaORiciclaBinding(convertView, parent)
        val struttura = getItem(position)!!
        val media = struttura.mediaRecensioni!!

        binding.apply {
            textViewNomeStrutturaRicerca.text = struttura.nomeStruttura
            if(struttura.distanzaDaUtente!! > 0f){
                textViewDistanzaStrutturaRicerca.text = ottieniStringaDistanza(struttura.distanzaDaUtente!!)
            }
            textViewTipoStruttura.text = struttura.tipoStruttura
            textViewIndirizzoStrutturaRicerca.text = struttura.indirizzo
            if(media > 0f){
                textViewMediaRecensioniRicerca.text = String.format("%.1f", media)
            }
            else {
                textViewMediaRecensioniRicerca.text = "N/A"
            }
        }
        impostaStelle(media, binding)
        return binding.root
    }

    private fun ottieniStringaDistanza(distanza: Float): String {
        if(distanza < 1000) {
            return String.format("%.0f m", distanza)
        }
        return String.format("%.1f km", distanza/1000)
    }

    private fun creaORiciclaBinding(convertView: View?, parent: ViewGroup): StruttureRowBinding {
        return if(convertView != null) {
            DataBindingUtil.getBinding(convertView)!!
        }
        else {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                    as LayoutInflater
            DataBindingUtil.inflate(inflater,
                R.layout.strutture_row, parent, false)
        }
    }

    private fun impostaStelle(valutazione: Float, binding: StruttureRowBinding) {
        binding.apply {
            imageViewStella1Struttura.setImageResource(ottieniStella( 1f, valutazione))
            imageViewStella2Struttura.setImageResource(ottieniStella( 2f, valutazione))
            imageViewStella3Struttura.setImageResource(ottieniStella( 3f, valutazione))
            imageViewStella4Struttura.setImageResource(ottieniStella( 4f, valutazione))
            imageViewStella5Struttura.setImageResource(ottieniStella( 5f, valutazione))
        }
    }

    private fun ottieniStella(numeroStella: Float, valutazione: Float): Int {
        return when {
            (numeroStella-valutazione < 0.3) -> R.drawable.ic_star_black_24dp
            (numeroStella-valutazione <= 0.75) -> R.drawable.ic_star_half_black_24dp
            else -> R.drawable.ic_star_border_black_24dp
        }
    }
}