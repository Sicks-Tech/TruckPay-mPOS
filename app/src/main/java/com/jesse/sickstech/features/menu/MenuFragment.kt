package com.jesse.sickstech.features.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jesse.sickstech.data.repository.OrderRepository
import com.jesse.sickstech.data.repository.ShopRepository
import com.jesse.sickstech.databinding.FragmentMenuBinding
import com.jesse.sickstech.domain.model.Menu
import com.jesse.sickstech.features.cart.CartActivity
import com.jesse.sickstech.features.menu.addons.AddonsDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val menuAdapter = MenuAdapter{ menuClicado ->
        viewModel.loadAddons(menuClicado.id, menuClicado.precoCents)
        abrirDetalhes(menuClicado)
    }

    private fun provideShopRepository(): ShopRepository {
        return ShopRepository.getInstance(requireContext())
    }

    private fun provideOrderRepository(): OrderRepository {
        return OrderRepository.getInstance(requireContext())
    }

    private val viewModel: MenuViewModel by activityViewModels {
        MenuViewModelFactory(provideShopRepository(), provideOrderRepository())
    }


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.loadProducts(1)


        binding.recyclerMenu.apply{
            binding.recyclerMenu.adapter = menuAdapter
            binding.recyclerMenu.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }


        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(requireContext(), CartActivity::class.java)
            startActivity(intent)
        }

        viewModel.menuItems.observe(viewLifecycleOwner) { lista ->
            menuAdapter.submitList(lista)
        }


    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

private fun MenuFragment.abrirDetalhes(menuClicado: Menu) {
    val dialog = AddonsDialogFragment()

//    Toast.makeText(requireContext(), "Clicou em ${menuClicado.titulo}", Toast.LENGTH_SHORT).show()

//    val bundle = Bundle().apply {
//        putInt("id", menuClicado.id)
//        putString("titulo", menuClicado.titulo)
//        putString("preco", menuClicado.preco)
//    }

    val bundle = Bundle().apply {
        putParcelable("menu", menuClicado) // menuClicado: Menu (com preco String)
    }

    dialog.arguments = bundle
    dialog.show(parentFragmentManager, "AddonsDialogFragment")
}





