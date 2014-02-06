package com.greenself.objects;

import com.greenself.daogen.TaskSource;

public final class Constants {

	//public static final int NO_OF_TASKS = 5;
	public static final int NO_OF_DAILY_TASKS = 4;
	public static final int NO_OF_WEEKLY_TASKS = 2;
	public static final int NO_OF_MONTHLY_TASKS = 1;
	public static final String DB_NAME = "task-db";
	public static final String SETTINGS_DONE_TASKS_VISIBILE = "done_tasks_visibility";
	public static final String LAST_WEEKLY_UPDATE = "last_weekly_update";
	public static final String LAST_MONTHLY_UPDATE = "last_monthly_update";
	public static final String APP = "com.greenself";
	public static final long BETWEEN_WEEKS = 7*24*60*60;
	public static final long BETWEEN_MONTHS = 30*24*60*60;

	// time recurrent frame
	public enum Type {
		DAILY, WEEKLY, MONTHLY, YEARLY
	};

	public static final TaskSource[] TASKS_IN_DB = new TaskSource[] {
			new TaskSource(
					true,
					Type.MONTHLY,
					"Change your light",
					"If every household in the United State replaced one regular lightbulb with one of those new compact fluorescent bulbs, the pollution reduction would be equivalent to removing one million cars from the road. Don't like the color of light? Use these bulbs for closets, laundry rooms and other places where it won't irk you as much."),
			new TaskSource(
					true,
					Type.DAILY,
					"Turn off computers at night",
					"By turning off your computer instead of leaving it in sleep mode, you can save 40 watt-hours per day. That adds up to 4 cents a day, or $14 per year. If you don't want to wait for your computer to start up, set it to turn on automatically a few minutes before you get to work, or boot up while you're pouring your morning cup 'o joe."),
			new TaskSource(
					true,
					Type.DAILY,
					"Don't rinse",
					"Skip rinsing dishes before using your dishwasher and save up to 20 gallons of water each load. Plus, you're saving time and the energy used to heat the additional water."),
			new TaskSource(
					true,
					Type.WEEKLY,
					"Don't pre-heat the oven",
					"Unless you are making bread or pastries of some sort, don't pre-heat the oven. Just turn it on when you put the dish in. Also, when checking on your food, look through the oven window instead of opening the door."),
			new TaskSource(
					true,
					Type.MONTHLY,
					"Recycle glass",
					"Recycled glass reduces related air pollution by 20 percent and related water pollution by 50 percent. If it isn't recycled it can take a million years to decompose."),
			new TaskSource(
					true,
					Type.DAILY,
					"Diaper with a conscience",
					"By the time a child is toilet trained, a parent will change between 5,000 and 8,000 diapers, adding up to approximately 3.5 million tons of waste in U.S. landfills each year. Whether you choose cloth or a more environmentally-friendly disposable, you're making a choice that has a much gentler impact on our planet."),
			new TaskSource(
					true,
					Type.WEEKLY,
					"Hang dry",
					"Get a clothesline or rack to dry your clothes by the air. Your wardrobe will maintain color and fit, and you'll save money. Your favorite t-shirt will last longer too."),
			new TaskSource(
					true,
					Type.WEEKLY,
					"Go vegetarian for today",
					"One less meat-based meal a week helps the planet and your diet. For example: It requires 2,500 gallons of water to produce one pound of beef. You will also also save some trees. For each hamburger that originated from animals raised on rainforest land, approximately 55 square feet of forest have been destroyed."),
			new TaskSource(
					true,
					Type.WEEKLY,
					"Wash in cold or warm water",
					"If all the households in the U.S. switched from hot-hot cycle to warm-cold, we could save the energy comparable to 100,000 barrels of oil a day."),
			new TaskSource(
					true,
					Type.DAILY,
					"Don't use paper napkins",
					"During an average year, an American uses approximately 2,200 napkins—around six each day. If everyone in the U.S. used one less napkin a day, more than a billion pounds of napkins could be saved from landfills each year."),
			new TaskSource(
					true,
					Type.DAILY,
					"Use both sides of paper",
					"American businesses throw away 21 million tons of paper every year, equal to 175 pounds per office worker. For a quick and easy way to halve this, set your printer's default option to print double-sided (duplex printing). And when you're finished with your documents, don't forget to take them to the recycling bin."),
			new TaskSource(
					true,
					Type.MONTHLY,
					"Recycle newspaper",
					"There are 63 million newspapers printed each day in the U.S. Of these, 44 million, or about 69%, of them will be thrown away. Recycling just the Sunday papers would save more than half a million trees every week."),
			new TaskSource(
					true,
					Type.MONTHLY,
					"Reusable water container",
					"Nearly 90% of plastic water bottles are not recycled, instead taking thousands of years to decompose. Buy a reusable container and fill it with tap water, a great choice for the environment, your wallet, and possibly your health. The EPA's standards for tap water are more stringent than the FDA's standards for bottled water."),
			new TaskSource(
					true,
					Type.WEEKLY,
					"Ban-bathtime! Take a shower",
					"Have a no-bath week, and take showers instead. Baths require almost twice as much water. Not only will you reduce water consumption, but the energy costs associated with heating the water."),
			new TaskSource(
					true,
					Type.DAILY,
					"Brush without running",
					"You've heard this one before, but maybe you still do it. You'll conserve up to five gallons per day if you stop. Daily savings in the U.S. alone could add up to 1.5 billion gallons--more water than folks use in the Big Apple."),
			new TaskSource(
					true,
					Type.WEEKLY,
					"Shower with your partner",
					"Sneak in a shower with your loved one to start the day with some zest that doesn't come in a bar. Not only have you made a wise choice for the environment, but you may notice some other added...um...benefits."),
			new TaskSource(
					true,
					Type.DAILY,
					"Shorten your shower time!",
					"Every two minutes you save on your shower can conserve more than ten gallons of water. If everyone in the country saved just one gallon from their daily shower, over the course of the year it would equal twice the amount of freshwater withdrawn from the Great Lakes every day."),
			new TaskSource(
					true,
					Type.YEARLY,
					"Plant a tree",
					"It's good for the air, the land, can shade your house and save on cooling (plant on the west side of your home), and they can also improve the value of your property. Make it meaningful for the whole family and plant a tree every year for each member."),
			new TaskSource(
					true,
					Type.WEEKLY,
					"Buy local",
					"Consider the amount of pollution created to get your food from the farm to your table. Whenever possible, buy from local farmers or farmers' markets, supporting your local economy and reducing the amount of greenhouse gas created when products are flown or trucked in."),
			new TaskSource(
					true,
					Type.MONTHLY,
					"Adjust your thermostat",
					"Adjust your thermostat one degree higher in the summer and one degree cooler in the winter. Each degree celsius less will save about 10% on your energy use! In addition, invest in a programmable thermostat which allows you to regulate temperature based on the times you are at home or away."),
			new TaskSource(
					true,
					Type.WEEKLY,
					"Invest in your own coffee cup",
					"If you start every morning with a steamy cup, a quick tabulation can show you that the waste is piling up. Invest in a reusable cup, which not only cuts down on waste, but keeps your beverage hot for a much longer time. Most coffee shops will happily fill your own cup, and many even offer you a discount in exchange!"),
			new TaskSource(
					true,
					Type.DAILY,
					"Turn off lights",
					"Always turn off incandescent bulbs when you leave a room. Fluorescent bulbs are more affected by the number of times it is switched on and off, so turn them off when you leave a room for 15 minutes or more. You'll save energy on the bulb itself, but also on cooling costs, as lights contribute heat to a room."),
			new TaskSource(
					true,
					Type.YEARLY,
					"Recycle old phones",
					"The average cell phone lasts around 18 months, which means 130 million phones will be retired each year. If they go into landfills, the phones and their batteries introduce toxic substances into our environment. There are plenty of reputable programs where you can recycle your phone, many which benefit noble causes."),
			new TaskSource(
					true,
					Type.YEARLY,
					"Maintain your vehicle",
					"Not only are you extending the life of your vehicle, but you are creating less pollution and saving gas. A properly maintained vehicle, clean air filters, and inflated tires can greatly improve your vehicle's performance. And it might not hurt to clean out the trunk—all that extra weight could be costing you at the pump."),
			new TaskSource(
					true,
					Type.MONTHLY,
					"Recycle aluminium and glass",
					"Twenty recycled aluminium cans can be made with the energy it takes to manufacture one brand new one. Every ton of glass recycled saves the equivalent of nine gallons of fuel oil needed to make glass from virgin materials."),
			new TaskSource(
					true,
					Type.MONTHLY,
					"Cut down on junk mail",
					"Feel like you need to lose a few pounds? It might be your junk mail that's weighing you down. The average American receives 40 pounds of junk mail each year, destroying 100 millions trees. There are many services that can help reduce the clutter in your mailbox, saving trees and the precious space on your countertops."),
			new TaskSource(
					true,
					Type.MONTHLY,
					"Use online services",
					"Consider if you really need a paper phone book. If not, call to stop phone book delivery and use an online directory instead. Some estimate that telephone books make up almost ten percent of waste at dump sites. And if you still receive the book, don't forget to recycle your old volumes."),
			new TaskSource(
					true,
					Type.MONTHLY,
					"Don't wash the car yourself",
					"Professional car washes are often more efficient with water consumption. If everyone in the U.S. who washes their car themselves took just one visit to the car wash we could save nearly 8.7 billion gallons of water."),
			new TaskSource(
					true,
					Type.WEEKLY,
					"Plastic bags suck",
					"Each year the U.S. uses 84 billion plastic bags, a significant portion of the 500 billion used worldwide. They are not biodegradable, and are making their way into our oceans, and subsequently, the food chain. Stronger, reusable bags are an inexpensive and readily available option."),
			new TaskSource(
					true,
					Type.WEEKLY,
					"Use e-tickets",
					"The cost of processing a paper ticket is approximately $10, while processing an e-ticket costs only $1. In the near future, e-tickets will be the only option, saving the airline industry $3 billion a year. In addition to financial savings, the sheer amount of paper eliminated by this process is commendable."),
			new TaskSource(
					true,
					Type.DAILY,
					"Skip the coffee stirrer",
					"Each year, Americans throw away 138 billion straws and stirrers. But skipping the stirrer doesn't mean drinking your coffee black. Simply put your sugar and cream in first, and then pour in the coffee, and it should be well mixed. Determined to stir? Break off a piece of pasta from the cupboard. You can nibble after using it, compost, or throw away with less guilt."),
			new TaskSource(
					true,
					Type.MONTHLY,
					"Pay bills online",
					"By some estimates, if all households in the U.S. paid their bills online and received electronic statements instead of paper, we'd save 18.5 million trees every year, 2.2 billion tons of carbon dioxide and other greenhouse gases, and 1.7 billion pounds of solid waste."),
			new TaskSource(
					true,
					Type.MONTHLY,
					"Stop paper bank statements",
					"Some banks will pay you a dollar or donate money on your behalf when you cancel the monthly paper statements you get in the mail. If every household took advantage of online bank statements, the money saved could send more than seventeen thousand recent high school graduates to a public university for a year."),
			new TaskSource(
					true,
					Type.WEEKLY,
					"Use rechargable batteries",
					"Each year 15 billion batteries produced and sold and most of them are disposable alkaline batteries. Only a fraction of those are recycled. Buy a charger and a few sets of rechargeable batteries. Although it requires an upfront investment, it is one that should pay off in no time. And on Christmas morning when all the stores are closed? You'll be fully stocked."), };

}
