package one_week_reparation_kit.day_01

/*
 * Complete the 'timeConversion' function below.
 *
 * The function is expected to return a STRING.
 * The function accepts STRING s as parameter.
 */

fun timeConversion(s: String): String {
    // Write your code here
    val getHour = s.substring(0, 2)
    val getMinutesAndSeconds = s.substring(2, 8)
    val getZone = s.substring(8, 10)
    val getFullTime = s.substring(0, 8)

    return if (getZone.equals("AM"))
        if (getHour == "12") "00$getMinutesAndSeconds" else getFullTime
    else
        if (getHour == "12") getFullTime else "${getHour.toInt() + 12}$getMinutesAndSeconds"
}
