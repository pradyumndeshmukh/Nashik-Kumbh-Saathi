package com.example.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.ui.AppLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.ConcurrentHashMap

object LanguageManager {

    private val dynamicCache = ConcurrentHashMap<Pair<String, AppLanguage>, String>()

    private val translations = mapOf(
        // Auth Screen
        "auth_login_with_number" to mapOf(
            AppLanguage.ENGLISH to "Login with Number",
            AppLanguage.HINDI to "नंबर से लॉगिन करें",
            AppLanguage.MARATHI to "नंबरने लॉगिन करा",
            AppLanguage.GUJARATI to "નંબરથી લોગિન કરો",
            AppLanguage.TAMIL to "எண் மூலம் உள்நுழைக",
            AppLanguage.TELUGU to "నంబర్‌తో లాగిన్ చేయండి",
            AppLanguage.BENGALI to "নম্বর দিয়ে লগইন করুন",
            AppLanguage.KANNADA to "ಸಂಖ್ಯೆಯೊಂದಿಗೆ ಲಾಗಿನ್ ಮಾಡಿ"
        ),
        "auth_login_with_email" to mapOf(
            AppLanguage.ENGLISH to "Login with Email",
            AppLanguage.HINDI to "ईमेल से लॉगिन करें",
            AppLanguage.MARATHI to "ईमेलने लॉगिन करा",
            AppLanguage.GUJARATI to "ઇમેઇલથી લોગિન કરો",
            AppLanguage.TAMIL to "மின்னஞ்சல் மூலம் உள்நுழைக",
            AppLanguage.TELUGU to "ఈమెయిల్‌తో లాగిన్ చేయండి",
            AppLanguage.BENGALI to "ইমেল দিয়ে লগইন করুন",
            AppLanguage.KANNADA to "ಇಮೇಲ್‌ನೊಂದಿಗೆ ಲಾಗಿನ್ ಮಾಡಿ"
        ),
        "auth_select_language" to mapOf(
            AppLanguage.ENGLISH to "Select App Language",
            AppLanguage.HINDI to "ऐप की भाषा चुनें",
            AppLanguage.MARATHI to "अ‍ॅपची भाषा निवडा",
            AppLanguage.GUJARATI to "એપ્લિકેશન ભાષા પસંદ કરો",
            AppLanguage.TAMIL to "பயன்பாட்டு மொழியைத் தேர்ந்தெடுக்கவும்",
            AppLanguage.TELUGU to "యాప్ భాషను ఎంచుకోండి",
            AppLanguage.BENGALI to "অ্যাপের ভাষা নির্বাচন করুন",
            AppLanguage.KANNADA to "ಆ್ಯಪ್ ಭಾಷೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ"
        ),
        "auth_sign_in" to mapOf(
            AppLanguage.ENGLISH to "Sign In",
            AppLanguage.HINDI to "साइन इन",
            AppLanguage.MARATHI to "साइन इन",
            AppLanguage.GUJARATI to "સાઇન ઇન",
            AppLanguage.TAMIL to "உள்நுழை",
            AppLanguage.TELUGU to "సైన్ ఇన్",
            AppLanguage.BENGALI to "সাইন ইন",
            AppLanguage.KANNADA to "ಸೈನ್ ಇನ್"
        ),
        "auth_sign_up" to mapOf(
            AppLanguage.ENGLISH to "Sign Up",
            AppLanguage.HINDI to "पंजीकरण करें",
            AppLanguage.MARATHI to "नोंदणी करा",
            AppLanguage.GUJARATI to "સાઇન અપ્ર",
            AppLanguage.TAMIL to "பதிவு செய்",
            AppLanguage.TELUGU to "సైన్ అప్",
            AppLanguage.BENGALI to "সাইন আপ",
            AppLanguage.KANNADA to "ಸೈನ್ ಅಪ್"
        ),
        "auth_welcome_title" to mapOf(
            AppLanguage.ENGLISH to "Nashik Kumbh Mela 2027",
            AppLanguage.HINDI to "नाशिक कुंभ मेला 2027",
            AppLanguage.MARATHI to "नाशिक कुंभमेळा २०२७",
            AppLanguage.GUJARATI to "નાસિક કુંભ મેળો 2027",
            AppLanguage.TAMIL to "நாசிக் கும்பமேளா 2027",
            AppLanguage.TELUGU to "నాసిక్ కుంభమేళా 2027",
            AppLanguage.BENGALI to "নাসিক কুম্ভ মেলা ২০২৭",
            AppLanguage.KANNADA to "ನಾಸಿಕ್ ಕುಂಭಮೇಳ 2027"
        ),
        "auth_subtitle" to mapOf(
            AppLanguage.ENGLISH to "Fast & Secure Pilgrim Portal",
            AppLanguage.HINDI to "त्वरित और सुरक्षित तीर्थयात्री पोर्टल",
            AppLanguage.MARATHI to "जलद व सुरक्षित यात्रेकरू पोर्टल",
            AppLanguage.GUJARATI to "ઝડપી અને સુરક્ષિત પોર્ટલ",
            AppLanguage.TAMIL to "வேகமான மற்றும் பாதுகாப்பான தளம்",
            AppLanguage.TELUGU to "వేగవంతమైన & సురక్షిత పోర్టల్",
            AppLanguage.BENGALI to "দ্রুত ও সুরক্ষিত পোর্টাল",
            AppLanguage.KANNADA to "ವೇಗದ ಮತ್ತು ಸುರಕ್ಷಿತ ಪೋರ್ಟಲ್"
        ),
        "auth_phone_number" to mapOf(
            AppLanguage.ENGLISH to "Mobile Phone Number",
            AppLanguage.HINDI to "मोबाइल फोन नंबर",
            AppLanguage.MARATHI to "मोबाईल फोन नंबर",
            AppLanguage.GUJARATI to "મોબાઇલ નંબર",
            AppLanguage.TAMIL to "கைபேசி எண்",
            AppLanguage.TELUGU to "మొబైల్ ఫోన్ నంబరు",
            AppLanguage.BENGALI to "মোবাইল নম্বর",
            AppLanguage.KANNADA to "ಮೊಬೈಲ್ ಸಂಖ್ಯೆ"
        ),
        "auth_send_otp" to mapOf(
            AppLanguage.ENGLISH to "Send Verification OTP",
            AppLanguage.HINDI to "ओटीपी भेजें",
            AppLanguage.MARATHI to "OTP पाठवा",
            AppLanguage.GUJARATI to "OTP મોકલો",
            AppLanguage.TAMIL to "OTP அனுப்பு",
            AppLanguage.TELUGU to "OTP పంపు",
            AppLanguage.BENGALI to "OTP পাঠান",
            AppLanguage.KANNADA to "OTP ಕಳುಹಿಸಿ"
        ),
        "auth_enter_otp" to mapOf(
            AppLanguage.ENGLISH to "Enter 6-Digit Verification OTP",
            AppLanguage.HINDI to "6-अंकों का ओटीपी दर्ज करें",
            AppLanguage.MARATHI to "6-अंकी OTP प्रविष्ट करा",
            AppLanguage.GUJARATI to "6-અંકનો OTP લખો",
            AppLanguage.TAMIL to "6-இலக்க OTP ஐ உள்ளிடவும்",
            AppLanguage.TELUGU to "6-అంకెల OTP ఎంటర్ చేయండి",
            AppLanguage.BENGALI to "৬-সংখ্যার OTP দিন",
            AppLanguage.KANNADA to "6-ಅಂಕಿಯ OTP ನಮೂದಿಸಿ"
        ),
        "auth_verify_otp" to mapOf(
            AppLanguage.ENGLISH to "Verify OTP & Sign In",
            AppLanguage.HINDI to "ओटीपी सत्यापित करें व साइन इन करें",
            AppLanguage.MARATHI to "OTP सत्यापित करा आणि साइन इन करा",
            AppLanguage.GUJARATI to "OTP ચકાસો અને સાઇન ઇન કરો",
            AppLanguage.TAMIL to "OTP சரிபார்த்து உள்நுழைக",
            AppLanguage.TELUGU to "OTP సరిచూసి సైన్ ఇన్ చేయండి",
            AppLanguage.BENGALI to "OTP যাচাই করে সাইন ইন করুন",
            AppLanguage.KANNADA to "OTP ಪರಿಶೀಲಿಸಿ ಸೈನ್ ಇನ್ ಮಾಡಿ"
        ),
        "auth_full_name" to mapOf(
            AppLanguage.ENGLISH to "Full Pilgrim Name",
            AppLanguage.HINDI to "तीर्थयात्री का पूरा नाम",
            AppLanguage.MARATHI to "यात्रेकरूचे पूर्ण नाव",
            AppLanguage.GUJARATI to "યાત્રીનું પૂરું નામ",
            AppLanguage.TAMIL to "யாத்ரீகரின் முழு பெயர்",
            AppLanguage.TELUGU to "యాత్రికుని పూర్తి పేరు",
            AppLanguage.BENGALI to "যাত্রীর পুরো নাম",
            AppLanguage.KANNADA to "ಯಾತ್ರಿಕರ ಪೂರ್ಣ ಹೆಸರು"
        ),
        "auth_state" to mapOf(
            AppLanguage.ENGLISH to "State of Origin",
            AppLanguage.HINDI to "मूल राज्य",
            AppLanguage.MARATHI to "मूळ राज्य",
            AppLanguage.GUJARATI to "વતન રાજ્ય",
            AppLanguage.TAMIL to "சொந்த மாநிலம்",
            AppLanguage.TELUGU to "స్వంత రాష్ట్రం",
            AppLanguage.BENGALI to "নিজের রাজ্য",
            AppLanguage.KANNADA to "ಸ್ವಂತ ರಾಜ್ಯ"
        ),
        "auth_emergency_contact" to mapOf(
            AppLanguage.ENGLISH to "Emergency Contact Number",
            AppLanguage.HINDI to "आपातकालीन संपर्क नंबर",
            AppLanguage.MARATHI to "तातडीचा संपर्क क्रमांक",
            AppLanguage.GUJARATI to "ઇમરજન્સી નંબર",
            AppLanguage.TAMIL to "அவசர தொடர்பு எண்",
            AppLanguage.TELUGU to "అత్యవసర కాంటాక్ట్ నంబర్",
            AppLanguage.BENGALI to "জরুরি যোগাযোগের নম্বর",
            AppLanguage.KANNADA to "ತುರ್ತು ಸಂಪರ್ಕ ಸಂಖ್ಯೆ"
        ),
        "auth_email" to mapOf(
            AppLanguage.ENGLISH to "Email Address",
            AppLanguage.HINDI to "ईमेल पता",
            AppLanguage.MARATHI to "ईमेल पत्ता",
            AppLanguage.GUJARATI to "ઈમેલ સરનામું",
            AppLanguage.TAMIL to "மின்னஞ்சல் முகவரி",
            AppLanguage.TELUGU to "ఈమెయిల్ చిరునామా",
            AppLanguage.BENGALI to "ইমেল ঠিকানা",
            AppLanguage.KANNADA to "ಇಮೇಲ್ ವಿಳಾಸ"
        ),
        "auth_password" to mapOf(
            AppLanguage.ENGLISH to "Password",
            AppLanguage.HINDI to "पासवर्ड",
            AppLanguage.MARATHI to "पासवर्ड",
            AppLanguage.GUJARATI to "પાસવર્ડ",
            AppLanguage.TAMIL to "கடவுச்சொல்",
            AppLanguage.TELUGU to "పాస్‌వర్డ్",
            AppLanguage.BENGALI to "পাসওয়ার্ড",
            AppLanguage.KANNADA to "ಪಾಸ್‌ವರ್ಡ್"
        ),
        "auth_use_phone_otp" to mapOf(
            AppLanguage.ENGLISH to "Login with Phone OTP",
            AppLanguage.HINDI to "फ़ोन ओटीपी से लॉगिन करें",
            AppLanguage.MARATHI to "फोन OTP द्वारे लॉगिन करा",
            AppLanguage.GUJARATI to "ફોન OTP વડે લોગિન કરો",
            AppLanguage.TAMIL to "தொலைபேசி OTP மூலம் உள்நுழைக",
            AppLanguage.TELUGU to "ఫోన్ OTP తో సైన్ ఇన్ చేయండి",
            AppLanguage.BENGALI to "ফোন OTP দিয়ে সাইন ইন",
            AppLanguage.KANNADA to "ಫೋನ್ OTP ಯೊಂದಿಗೆ ಸೈನ್ ಇನ್ ಮಾಡಿ"
        ),
        "auth_use_email_pass" to mapOf(
            AppLanguage.ENGLISH to "Login with Email & Password",
            AppLanguage.HINDI to "ईमेल और पासवर्ड से लॉगिन करें",
            AppLanguage.MARATHI to "ईमेल आणि पासवर्डने लॉगिन करा",
            AppLanguage.GUJARATI to "ઈમેલ અને પાસવર્ડ વડે લોગિન કરો",
            AppLanguage.TAMIL to "மின்னஞ்சல் & கடவுச்சொல் மூலம் உள்நுழைக",
            AppLanguage.TELUGU to "ఈమెయిల్ & పాస్‌వర్డ్‌తో సైన్ ఇన్ చేయండి",
            AppLanguage.BENGALI to "ইমেল ও পাসওয়ার্ড দিয়ে সাইন ইন",
            AppLanguage.KANNADA to "ಇಮೇಲ್ ಮತ್ತು ಪಾಸ್‌ವರ್ಡ್‌ನೊಂದಿಗೆ ಸೈನ್ ಇನ್ ಮಾಡಿ"
        ),
        "auth_logged_in_as" to mapOf(
            AppLanguage.ENGLISH to "Logged In Pilgrim Profile",
            AppLanguage.HINDI to "लॉग इन तीर्थयात्री प्रोफाइल",
            AppLanguage.MARATHI to "लॉग इन केलेले प्रोफाईल",
            AppLanguage.GUJARATI to "લોગ ઇન પ્રોફાઇલ",
            AppLanguage.TAMIL to "உள்நுழைந்துள்ள சுயவிவரம்",
            AppLanguage.TELUGU to "లాగిన్ అయిన యాత్రికుని ప్రొఫైల్",
            AppLanguage.BENGALI to "লগ ইন করা প্রোফাইল",
            AppLanguage.KANNADA to "ಲಾಗಿನ್ ಆದ ಪ್ರೊಫೈಲ್"
        ),
        "auth_logout" to mapOf(
            AppLanguage.ENGLISH to "Logout",
            AppLanguage.HINDI to "लॉगआउट",
            AppLanguage.MARATHI to "लॉगआउट",
            AppLanguage.GUJARATI to "લોગઆઉટ",
            AppLanguage.TAMIL to "வெளியேறு",
            AppLanguage.TELUGU to "లాగ్ అవుట్",
            AppLanguage.BENGALI to "লগઆઉટ",
            AppLanguage.KANNADA to "ಲಾಗ್‌ಔಟ್"
        ),

        // Navigation Tabs
        "tab_home" to mapOf(
            AppLanguage.ENGLISH to "Home",
            AppLanguage.HINDI to "मुख्य पृष्ठ",
            AppLanguage.MARATHI to "मुख्य",
            AppLanguage.GUJARATI to "હોમ",
            AppLanguage.TAMIL to "முகப்பு",
            AppLanguage.TELUGU to "హోమ్",
            AppLanguage.BENGALI to "হোম",
            AppLanguage.KANNADA to "ಹೋಮ್"
        ),
        "tab_schedule" to mapOf(
            AppLanguage.ENGLISH to "Snan Dates",
            AppLanguage.HINDI to "स्नान तिथियां",
            AppLanguage.MARATHI to "स्नान वेळापत्रक",
            AppLanguage.GUJARATI to "સ્નાન તારીખો",
            AppLanguage.TAMIL to "ஸ்நான தேதிகள்",
            AppLanguage.TELUGU to "స్నాన్ తేదీలు",
            AppLanguage.BENGALI to "স্নানের তারিখ",
            AppLanguage.KANNADA to "ಸ್ನಾನದ ದಿನಾಂಕಗಳು"
        ),
        "tab_map" to mapOf(
            AppLanguage.ENGLISH to "Kumbh Map",
            AppLanguage.HINDI to "कुंभ नक्शा",
            AppLanguage.MARATHI to "कुंभ नकाशा",
            AppLanguage.GUJARATI to "કુંભ નકશો",
            AppLanguage.TAMIL to "கும்ப வரைபடம்",
            AppLanguage.TELUGU to "కుంభ మ్యాప్",
            AppLanguage.BENGALI to "কুম্ভ মানচিত্র",
            AppLanguage.KANNADA to "ಕುಂಭ ನಕ್ಷೆ"
        ),
        "tab_stay" to mapOf(
            AppLanguage.ENGLISH to "Stays & Hotels",
            AppLanguage.HINDI to "होटल व आवास",
            AppLanguage.MARATHI to "हॉटेल्स व निवास",
            AppLanguage.GUJARATI to "રહેઠાણ અને હોટેલ્સ",
            AppLanguage.TAMIL to "தங்கும் இடம்",
            AppLanguage.TELUGU to "హోటళ్ళు & నివాసం",
            AppLanguage.BENGALI to "থাকার জায়গা",
            AppLanguage.KANNADA to "ಉಳಿದುಕೊಳ್ಳುವ ಸ್ಥಳ"
        ),
        "tab_food" to mapOf(
            AppLanguage.ENGLISH to "Food & Restaurants",
            AppLanguage.HINDI to "भोजन व ढाबा",
            AppLanguage.MARATHI to "खाद्य व उपाहार",
            AppLanguage.GUJARATI to "રેસ્ટોરન્ટ્સ",
            AppLanguage.TAMIL to "உணவகங்கள்",
            AppLanguage.TELUGU to "రెస్టారెంట్లు",
            AppLanguage.BENGALI to "রেস্তোরাঁ ও খাবার",
            AppLanguage.KANNADA to "ಆಹಾರ ಮತ್ತು ಉಪಹಾರ"
        ),

        // Navigation Drawer Items
        "nav_transport" to mapOf(
            AppLanguage.ENGLISH to "Buses & Transport",
            AppLanguage.HINDI to "बस व परिवहन",
            AppLanguage.MARATHI to "बस व वाहतूक",
            AppLanguage.GUJARATI to "બસ અને પરિવહન",
            AppLanguage.TAMIL to "பேருந்து & போக்குவரத்து",
            AppLanguage.TELUGU to "బస్సులు & రవాణా",
            AppLanguage.BENGALI to "বাস ও পরিবহন",
            AppLanguage.KANNADA to "ಬಸ್ ಮತ್ತು ಸಾರಿಗೆ"
        ),
        "nav_medical" to mapOf(
            AppLanguage.ENGLISH to "Medicals & Hospitals",
            AppLanguage.HINDI to "अस्पताल व स्वास्थ्य",
            AppLanguage.MARATHI to "वैद्यकीय व रुग्णालये",
            AppLanguage.GUJARATI to "તબીબી સેવાઓ",
            AppLanguage.TAMIL to "மருத்துவம் & மருத்துவமனை",
            AppLanguage.TELUGU to "వైద్యశాలలు & ఆసుపత్రులు",
            AppLanguage.BENGALI to "হাসপাতাল ও চিকিৎসা",
            AppLanguage.KANNADA to "ಆಸ್ಪತ್ರೆಗಳು"
        ),
        "nav_emergency" to mapOf(
            AppLanguage.ENGLISH to "Emergency & SOS",
            AppLanguage.HINDI to "आपातकालीन व एसओएस",
            AppLanguage.MARATHI to "तातडीची मदत व SOS",
            AppLanguage.GUJARATI to "ઈમરજન્સી અને SOS",
            AppLanguage.TAMIL to "அவசரம் & SOS",
            AppLanguage.TELUGU to "అత్యవసరం & SOS",
            AppLanguage.BENGALI to "জরুরি সেবা ও SOS",
            AppLanguage.KANNADA to "ತುರ್ತು ಸೇವೆ & SOS"
        ),
        "nav_culture" to mapOf(
            AppLanguage.ENGLISH to "Culture & Akharas",
            AppLanguage.HINDI to "संस्कृति व अखाड़े",
            AppLanguage.MARATHI to "संस्कृती व आखाडे",
            AppLanguage.GUJARATI to "સંસ્કૃતિ અને અખાડા",
            AppLanguage.TAMIL to "கலாச்சாரம் & அகாரா",
            AppLanguage.TELUGU to "సంస్కృతి & అఖాడాలు",
            AppLanguage.BENGALI to "সংস্কৃতি ও আখাড়া",
            AppLanguage.KANNADA to "ಸಂಸ್ಕೃತಿ"
        ),
        "nav_profile" to mapOf(
            AppLanguage.ENGLISH to "My Profile & Yatra Pass",
            AppLanguage.HINDI to "मेरी प्रोफाइल व यात्रा पास",
            AppLanguage.MARATHI to "माझी प्रोफाईल व यात्रा पास",
            AppLanguage.GUJARATI to "મારી પ્રોફાઇલ અને પાસ",
            AppLanguage.TAMIL to "என் சுயவிவரம் & பாஸ்",
            AppLanguage.TELUGU to "నా ప్రొఫైల్ & యాత్రా పాస్",
            AppLanguage.BENGALI to "আমার প্রোফাইল ও পাস",
            AppLanguage.KANNADA to "ನನ್ನ ಪ್ರೊಫೈಲ್"
        ),

        // Common UI Actions & Titles
        "search_placeholder" to mapOf(
            AppLanguage.ENGLISH to "Search Ghats, Temples, Buses, Hotels...",
            AppLanguage.HINDI to "घाट, मंदिर, बस, होटल खोजें...",
            AppLanguage.MARATHI to "घाट, मंदिरे, बस, हॉटेल्स शोधा...",
            AppLanguage.GUJARATI to "ઘાટ, મંદિર, બસ, હોટેલ શોધો...",
            AppLanguage.TAMIL to "காட், கோவில், பேருந்து, தங்கும் இடம் தேடுக...",
            AppLanguage.TELUGU to "ఘాట్‌లు, గుడులు, బస్సులు, హోటళ్లు వెతకండి...",
            AppLanguage.BENGALI to "ঘাট, মন্দির, বাস, হোটেল খুঁজুন...",
            AppLanguage.KANNADA to "ಘಾಟ್‌ಗಳು, ದೇವಾಲಯಗಳು, ಬಸ್‌ಗಳನ್ನು ಹುಡುಕಿ..."
        ),
        "live_crowd_title" to mapOf(
            AppLanguage.ENGLISH to "Live Crowd Density Advisory",
            AppLanguage.HINDI to "लाइव भीड़ नियंत्रण चेतावनी",
            AppLanguage.MARATHI to "थेट गर्दी नियंत्रण सूचना",
            AppLanguage.GUJARATI to "લાઇવ ભીડ સૂચના",
            AppLanguage.TAMIL to "நேரடி கூட்ட நெரிசல் எச்சரிக்கை",
            AppLanguage.TELUGU to "లైవ్ రద్దీ సలహా",
            AppLanguage.BENGALI to "লাইভ ভিড় সতর্কতা",
            AppLanguage.KANNADA to "ನೇರ ಜನಸಂದಣಿ ಸಲಹೆ"
        ),
        "shahi_snan_countdown" to mapOf(
            AppLanguage.ENGLISH to "Shahi Snan Countdown",
            AppLanguage.HINDI to "शाही स्नान उल्टी गिनती",
            AppLanguage.MARATHI to "शाही स्नान काउंटडाऊन",
            AppLanguage.GUJARATI to "શાહી સ્નાન સમય",
            AppLanguage.TAMIL to "ஷாஹி ஸ்நானம் கவுண்டவுன்",
            AppLanguage.TELUGU to "షాహి స్నాన్ కౌంట్‌డౌన్",
            AppLanguage.BENGALI to "শাহী স্নান কাউন্টডাউন",
            AppLanguage.KANNADA to "ಷಾಹಿ ಸ್ನಾನ ಕೌಂಟ್‌ಡೌನ್"
        ),
        "unit_days" to mapOf(
            AppLanguage.ENGLISH to "Days",
            AppLanguage.HINDI to "दिन",
            AppLanguage.MARATHI to "दिवस",
            AppLanguage.GUJARATI to "દિવસો",
            AppLanguage.TAMIL to "நாட்கள்",
            AppLanguage.TELUGU to "రోజులు",
            AppLanguage.BENGALI to "দিন",
            AppLanguage.KANNADA to "ದಿನಗಳು"
        ),
        "unit_hours" to mapOf(
            AppLanguage.ENGLISH to "Hours",
            AppLanguage.HINDI to "घंटे",
            AppLanguage.MARATHI to "तास",
            AppLanguage.GUJARATI to "કલાક",
            AppLanguage.TAMIL to "மணி",
            AppLanguage.TELUGU to "గంటలు",
            AppLanguage.BENGALI to "ঘণ্টা",
            AppLanguage.KANNADA to "ಗಂಟೆಗಳು"
        ),
        "unit_mins" to mapOf(
            AppLanguage.ENGLISH to "Mins",
            AppLanguage.HINDI to "मिनट",
            AppLanguage.MARATHI to "मिनिटे",
            AppLanguage.GUJARATI to "મિનિટ",
            AppLanguage.TAMIL to "நிமிடம்",
            AppLanguage.TELUGU to "నిమిషాలు",
            AppLanguage.BENGALI to "মিনিট",
            AppLanguage.KANNADA to "ನಿಮಿಷಗಳು"
        ),
        "unit_secs" to mapOf(
            AppLanguage.ENGLISH to "Secs",
            AppLanguage.HINDI to "सेकंड",
            AppLanguage.MARATHI to "सेकंद",
            AppLanguage.GUJARATI to "સેકન્ડ",
            AppLanguage.TAMIL to "நொடி",
            AppLanguage.TELUGU to "సెకన్లు",
            AppLanguage.BENGALI to "সেকেন্ড",
            AppLanguage.KANNADA to "ಸೆಕೆಂಡುಗಳು"
        ),
        "btn_call_now" to mapOf(
            AppLanguage.ENGLISH to "Call Now",
            AppLanguage.HINDI to "अभी कॉल करें",
            AppLanguage.MARATHI to "आत्ताच कॉल करा",
            AppLanguage.GUJARATI to "કોલ કરો",
            AppLanguage.TAMIL to "இப்போதே அழைக்கவும்",
            AppLanguage.TELUGU to "ఇప్పుడే కాల్ చేయండి",
            AppLanguage.BENGALI to "এখনই কল করুন",
            AppLanguage.KANNADA to "ಈಗ ಕರೆ ಮಾಡಿ"
        ),
        "btn_open_maps" to mapOf(
            AppLanguage.ENGLISH to "Google Maps",
            AppLanguage.HINDI to "गूगल मैप्स",
            AppLanguage.MARATHI to "गुगल मॅप्स",
            AppLanguage.GUJARATI to "ગૂગલ મેપ્સ",
            AppLanguage.TAMIL to "கூகுள் மேப்ஸ்",
            AppLanguage.TELUGU to "గూగుల్ మ్యాప్స్",
            AppLanguage.BENGALI to "গুগল ম্যাপস",
            AppLanguage.KANNADA to "ಗೂಗಲ್ ಮ್ಯಾಪ್ಸ್"
        )
    )

    // Reverse lookup map from English string -> key
    private val englishValueToKeyMap: Map<String, String> by lazy {
        val map = mutableMapOf<String, String>()
        translations.forEach { (key, langMap) ->
            langMap[AppLanguage.ENGLISH]?.let { engText ->
                map[engText.trim().lowercase()] = key
            }
        }
        map
    }

    fun getString(keyOrText: String, language: AppLanguage): String {
        if (keyOrText.isBlank()) return keyOrText
        if (language == AppLanguage.ENGLISH) {
            val fromKey = translations[keyOrText]?.get(AppLanguage.ENGLISH)
            return fromKey ?: keyOrText
        }

        // 1. Direct key match
        translations[keyOrText]?.get(language)?.let { return it }

        // 2. English text lookup match
        val cleanEng = keyOrText.trim().lowercase()
        englishValueToKeyMap[cleanEng]?.let { key ->
            translations[key]?.get(language)?.let { return it }
        }

        // 3. Dynamic cache check
        val cached = dynamicCache[Pair(keyOrText, language)]
        if (cached != null) return cached

        return keyOrText
    }

    suspend fun translateOnline(text: String, targetLang: AppLanguage): String = withContext(Dispatchers.IO) {
        if (text.isBlank() || targetLang == AppLanguage.ENGLISH || text.all { it.isDigit() || it.isWhitespace() || !it.isLetter() }) {
            return@withContext text
        }

        val cacheKey = Pair(text, targetLang)
        dynamicCache[cacheKey]?.let { return@withContext it }

        try {
            val encodedText = URLEncoder.encode(text, "UTF-8")
            val urlString = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=${targetLang.code}&dt=t&q=$encodedText"
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            if (connection.responseCode == 200) {
                val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(responseText)
                val sentences = jsonArray.getJSONArray(0)
                val sb = StringBuilder()
                for (i in 0 until sentences.length()) {
                    val sentence = sentences.getJSONArray(i)
                    if (sentence.length() > 0) {
                        sb.append(sentence.getString(0))
                    }
                }
                val translatedText = sb.toString().trim()
                if (translatedText.isNotBlank()) {
                    dynamicCache[cacheKey] = translatedText
                    return@withContext translatedText
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext text
    }
}

@Composable
fun String.tr(language: AppLanguage): String {
    if (this.isBlank()) {
        return this
    }

    val instantTranslation = LanguageManager.getString(this, language)
    if (instantTranslation != this) {
        return instantTranslation
    }

    if (language == AppLanguage.ENGLISH) {
        return this
    }

    val translatedState = remember(this, language) { mutableStateOf(instantTranslation) }

    LaunchedEffect(this, language) {
        if (translatedState.value == this@tr) {
            val result = LanguageManager.translateOnline(this@tr, language)
            if (result != this@tr) {
                translatedState.value = result
            }
        }
    }

    return translatedState.value
}
