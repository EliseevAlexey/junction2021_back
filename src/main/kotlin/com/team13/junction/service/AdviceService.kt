package com.team13.junction.service

import org.springframework.stereotype.Service

@Service
class AdviceService {

    fun getAdvice(): String =
        suggestions.random()

    companion object {
        private val suggestions = listOf(
            "Check your toilet for leaks.",
            "Stop using your toilet as an ashtray or wastebasket",
            "Put a plastic bottle in your toilet tank",
            "Take shorter showers",
            "Install water-saving shower heads or flow restrictors",
            "Take baths",
            "Turn off the water while brushing your teeth",
            "Turn off the water while shaving",
            "Check faucets and pipes for leaks",
            "Use your automatic dishwasher for full loads only",
            "Use your automatic washing machine only for full loads only",
            "Don't let the faucet run while you clean vegetables",
            "Keep a bottle of drinking water in the refrigerator",
            "If you wash dishes by hand, don't leave the water running for rinsing",
            "Check faucets and pipes for leaks",
            "Water your lawn only when it needs it",
            "Deep-soak your lawn",
            "Water during the cool parts of the day",
            "Don't water the gutter",
            "Plant drought-resistant trees and plants",
            "Put a layer of mulch around trees and plants.",
            "Use a broom to clean driveways, sidewalks and steps",
            "Don't run the hose while washing your car",
            "Tell your children not to play with the hose and sprinklers",
            "Check for leaks in pipes, hoses faucets and couplings"
        )
    }
}