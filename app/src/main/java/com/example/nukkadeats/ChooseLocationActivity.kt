package com.example.nukkadeats

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeats.databinding.ActivityChooseLocationBinding

class ChooseLocationActivity : AppCompatActivity() {

    private val binding : ActivityChooseLocationBinding by lazy {
        ActivityChooseLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(binding.root)

        val stateCityMap = mapOf(
            "Andhra Pradesh" to listOf("Visakhapatnam", "Vijayawada", "Guntur", "Nellore", "Tirupati", "Kakinada", "Anantapur"),
            "Arunachal Pradesh" to listOf("Itanagar", "Tawang", "Ziro", "Pasighat"),
            "Assam" to listOf("Guwahati", "Dibrugarh", "Silchar", "Jorhat", "Tezpur", "Nagaon"),
            "Bihar" to listOf("Patna", "Gaya", "Bhagalpur", "Muzaffarpur", "Darbhanga", "Purnia"),
            "Chhattisgarh" to listOf("Raipur", "Bilaspur", "Durg", "Korba", "Rajnandgaon"),
            "Goa" to listOf("Panaji", "Margao", "Vasco da Gama", "Mapusa"),
            "Gujarat" to listOf("Ahmedabad", "Surat", "Vadodara", "Rajkot", "Jamnagar", "Bhavnagar", "Gandhinagar"),
            "Haryana" to listOf("Gurgaon", "Faridabad", "Panipat", "Ambala", "Karnal", "Rohtak"),
            "Himachal Pradesh" to listOf("Shimla", "Manali", "Dharamshala", "Solan", "Mandi", "Kullu"),
            "Jharkhand" to listOf("Ranchi", "Jamshedpur", "Dhanbad", "Bokaro", "Deoghar"),
            "Karnataka" to listOf("Bangalore", "Mysore", "Hubli", "Mangalore", "Belgaum", "Tumkur", "Davangere"),
            "Kerala" to listOf("Kochi", "Thiruvananthapuram", "Kozhikode", "Thrissur", "Alappuzha", "Kollam"),
            "Madhya Pradesh" to listOf("Bhopal", "Indore", "Gwalior", "Jabalpur", "Ujjain", "Sagar", "Satna"),
            "Maharashtra" to listOf("Mumbai", "Pune", "Nagpur", "Nashik", "Aurangabad", "Solapur", "Amravati"),
            "Manipur" to listOf("Imphal", "Thoubal"),
            "Meghalaya" to listOf("Shillong", "Tura"),
            "Mizoram" to listOf("Aizawl", "Lunglei"),
            "Nagaland" to listOf("Kohima", "Dimapur", "Mokokchung"),
            "Odisha" to listOf("Bhubaneswar", "Cuttack", "Rourkela", "Sambalpur", "Puri", "Berhampur"),
            "Punjab" to listOf("Amritsar", "Ludhiana", "Jalandhar", "Patiala", "Bathinda"),
            "Rajasthan" to listOf("Jaipur", "Udaipur", "Jodhpur", "Ajmer", "Bikaner", "Kota", "Alwar"),
            "Sikkim" to listOf("Gangtok", "Namchi"),
            "Tamil Nadu" to listOf("Chennai", "Coimbatore", "Madurai", "Tiruchirappalli", "Salem", "Tirunelveli"),
            "Telangana" to listOf("Hyderabad", "Warangal", "Karimnagar", "Nizamabad"),
            "Tripura" to listOf("Agartala", "Udaipur"),
            "Uttar Pradesh" to listOf("Lucknow", "Kanpur", "Varanasi", "Agra", "Meerut", "Allahabad", "Noida", "Gorakhpur"),
            "Uttarakhand" to listOf("Dehradun", "Haridwar", "Nainital", "Haldwani", "Rishikesh"),
            "West Bengal" to listOf("Kolkata", "Howrah", "Durgapur", "Asansol", "Siliguri"),
            "Delhi" to listOf("New Delhi", "Dwarka", "Rohini", "Saket"),
            "Jammu & Kashmir" to listOf("Srinagar", "Jammu", "Baramulla", "Anantnag"),
            "Ladakh" to listOf("Leh", "Kargil")
        )


        val states = stateCityMap.keys.toList()
        val cities = stateCityMap.values.flatten().distinct().sorted()
        val stateAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, states)
        val cityAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cities)

        binding.stateAutoComplete.setAdapter(stateAdapter)
        binding.cityAutoComplete.setAdapter(cityAdapter)

        // State selection listener
        binding.stateAutoComplete.setOnItemClickListener { _, _, position, _ ->
            val selectedState = states[position]
            val cities = stateCityMap[selectedState] ?: emptyList()
            val newCityAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cities)
            binding.cityAutoComplete.setAdapter(newCityAdapter)
            binding.cityAutoComplete.text.clear()
        }

        // City selection listener
        binding.cityAutoComplete.setOnItemClickListener { _, _, position, _ ->
            val selectedCity = binding.cityAutoComplete.adapter.getItem(position) as String
            val foundState = stateCityMap.entries.find { it.value.contains(selectedCity) }?.key
            foundState?.let {
                binding.stateAutoComplete.setText(it, false)
            }

            val intent = Intent(this , MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        }
    }

