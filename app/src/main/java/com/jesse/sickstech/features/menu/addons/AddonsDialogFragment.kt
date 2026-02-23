package com.jesse.sickstech.features.menu.addons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jesse.sickstech.core.util.CurrencyFormatter
import com.jesse.sickstech.core.util.setupToolbar
import com.jesse.sickstech.databinding.FragmentAddonsDialogBinding
import com.jesse.sickstech.domain.model.Menu
import com.jesse.sickstech.features.menu.MenuViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddonsDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddonsDialogFragment : DialogFragment() {
    private var _binding: FragmentAddonsDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var addonsAdapter: AddonsAdapter
    private val viewModel: MenuViewModel by activityViewModels()
//    private val addonsRepository = AddonsRepository()

    private var id: Int = 0
    private var titulo: String? = null
    private var preco: String? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        arguments?.let { bundle ->
            val menu = bundle.getParcelable<Menu>("menu")

            id = menu?.id ?: 0
            titulo = menu?.titulo
            preco = menu?.precoCents?.toString()
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddonsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.addons.observe(viewLifecycleOwner){list ->
//            addonsAdapter.submitList(list)
//        }

        addonsAdapter = AddonsAdapter(
            onAdd = { addon ->
                viewModel.incrementAddon(addon.addon.id)
            },
            onSubtract = { addon ->
                viewModel.decrementAddon(addon.addon.id)
            }
        )

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                addonsAdapter.submitList(state.addons)
                binding.textViewTotal.text = CurrencyFormatter.format(state.total)
                binding.textViewAddonsTotal.text = CurrencyFormatter.format(state.addonsTotal)
            }
        }


        binding.includeToolbar.setupToolbar(
            title = "Adicionais",
            showKeyboard = false,
            onBack = { dismiss() }
        )



        binding.addonsRecyclerView.apply {
            binding.addonsRecyclerView.adapter = addonsAdapter
            binding.addonsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        with(binding) {
            textViewTitle.text = titulo
            textViewTotal.text = preco


            btnAdicionar.setOnClickListener {
                // logica é adicionar um item de carrinho depois fechar e dar um dialog com sucesso/falha
                // Pegamos o estado atual que contém tudo: produto, preços e adicionais
                val currentState = viewModel.state.value

                // Usamos o accountId fixo por enquanto (ou o que vier do login)
                val accountId = 1

                // Chamamos a nova função que trata de tudo
                viewModel.addItemToCart(accountId, currentState)


                dismiss()
                Toast.makeText(
                    this@AddonsDialogFragment.requireContext(),
                    "Item Adicionado com Sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddonsDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddonsDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}