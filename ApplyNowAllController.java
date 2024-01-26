package com.avanse.springboot.controller.globalPages;

import com.avanse.springboot.DTO.JobSearchResponseDTO;
import com.avanse.springboot.repository.forms.applyNow.ApplyNowGeneralRepository;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import org.springframework.http.ResponseEntity;
import com.avanse.springboot.DTO.UniversityDTO;
import com.avanse.springboot.DTO.forms.applyNow.ApplyNowGeneralDTO;
import com.avanse.springboot.DTO.forms.applyNow.ETutoringDTO;
import com.avanse.springboot.DTO.forms.applyNow.EducationInstitutionLoanDTO;
import com.avanse.springboot.DTO.forms.applyNow.ExecutiveEducationLoanDTO;
import com.avanse.springboot.DTO.forms.applyNow.PartnerWithUsDTO;
import com.avanse.springboot.DTO.forms.applyNow.SchoolFeeFinancingDTO;
import com.avanse.springboot.DTO.forms.applyNow.SkillEnhancementDTO;
import com.avanse.springboot.model.forms.applyNow.ApplyNowGeneral;
import com.avanse.springboot.model.forms.applyNow.ETutoring;
import com.avanse.springboot.model.forms.applyNow.EducationInstitutionLoan;
import com.avanse.springboot.model.forms.applyNow.ExecutiveEducationLoan;
import com.avanse.springboot.model.forms.applyNow.PartnerWithUs;
import com.avanse.springboot.model.forms.applyNow.SchoolFeeFinancing;
import com.avanse.springboot.model.forms.applyNow.SkillEnhancement;
import com.avanse.springboot.service.applyNow.ApplyNowGeneralService;
import com.avanse.springboot.service.applyNow.ETutoringService;
import com.avanse.springboot.service.applyNow.EducationInstitutionLoanService;
import com.avanse.springboot.service.applyNow.ExecutiveEducationLoanService;
import com.avanse.springboot.service.applyNow.PartnerWithUsService;
import com.avanse.springboot.service.applyNow.SchoolFeeFinancingService;
import com.avanse.springboot.service.applyNow.SkillEnhancementService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.thymeleaf.extras.springsecurity5.auth.Authorization;

import javax.xml.bind.JAXBException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
@Controller
public class ApplyNowAllController {
	URL url = new URL("https://fw-avanse.agiratech.com/api/v1/contacts");

	@Autowired
	ApplyNowGeneralService applyNowGeneralService;
	
	@Autowired
	ExecutiveEducationLoanService executiveEducationLoanService;
	
	@Autowired
	EducationInstitutionLoanService educationInstitutionLoanService;
	
	@Autowired
	ETutoringService eTutoringService;
	
	@Autowired
	PartnerWithUsService partnerWithUsService;
	
	@Autowired
	SchoolFeeFinancingService schoolFeeFinancingService;
	
	@Autowired
	SkillEnhancementService skillEnhancementService;

	@Autowired
	ApplyNowGeneralRepository applyNowGeneralRepository;

	public ApplyNowAllController() throws MalformedURLException {


	}
	/*public class OkHttpPost {

		public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

		OkHttpClient client = new OkHttpClient();

		String post(String url, String json) throws IOException {
			RequestBody body = RequestBody.create(JSON, json);


			Request request = new Request.Builder()
					.url("https://fw-avanse.agiratech.com/api/v1/contacts")
					.method("POST", body)
					.addHeader("Authorization", "Basic ZUxPaVM5WEdPbUgwMU5PaFdKcmlhUT09Olg=")
					.addHeader("Content-Type", "application/json")
					.build();
			try (Response response = client.newCall(request).execute()) {
				return response.body().string();
			}
		}
	}*/
	@PostMapping("/apply-now/thankyou")
	public String applyNowGeneralAddPost(@ModelAttribute("applyNowGeneralDTO") ApplyNowGeneralDTO applyNowGeneralDTO) throws IOException, JAXBException, JSONException {
		InetAddress IP=InetAddress.getLocalHost();
		//System.out.println("IP of my system is := "+IP.getHostAddress());
		ApplyNowGeneral applyNowGeneral = new ApplyNowGeneral();
		applyNowGeneral.setName(applyNowGeneralDTO.getName());
		applyNowGeneral.setCity(applyNowGeneralDTO.getCity());
		applyNowGeneral.setEmailId(applyNowGeneralDTO.getEmailId());
		applyNowGeneral.setContactNumber(applyNowGeneralDTO.getContactNumber());
		applyNowGeneral.setPlaceOfStudy(applyNowGeneralDTO.getPlaceOfStudy());
		applyNowGeneral.setLoanAmount(applyNowGeneralDTO.getLoanAmount());
		applyNowGeneral.setAdmissionStatus(applyNowGeneralDTO.getAdmissionStatus());
		applyNowGeneral.setPermission(applyNowGeneralDTO.getPermission());
		applyNowGeneral.setTimeOfStudy(applyNowGeneralDTO.getTimeOfStudy());
		applyNowGeneral.setSource(applyNowGeneralDTO.getSource());
		applyNowGeneral.setCampaignid(applyNowGeneralDTO.getCampaignid());
		applyNowGeneral.setAdgroup(applyNowGeneralDTO.getAdgroup());
		applyNowGeneral.setKeywords(applyNowGeneralDTO.getKeywords());
		applyNowGeneral.setGclid(applyNowGeneralDTO.getGclid());
		applyNowGeneral.setIp_address(IP.getHostAddress());
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		OkHttpClient client = new OkHttpClient().newBuilder()
				.build();
		MediaType mediaType = MediaType.parse("application/json");
		String content = "{\n\t\"first_name\": \"" + applyNowGeneralDTO.getName()
				+ "\",\n\t\"last_name\": \"" + applyNowGeneralDTO.getName()
				+ "\",\n\t\"email\": \"" + applyNowGeneralDTO.getEmailId()
				+ "\",\n\t\"mobile_number\": \"" + applyNowGeneralDTO.getContactNumber()
				+ "\",\n\t\"city\": \"" + applyNowGeneralDTO.getCity()
				+ "\",\n\t\"check_dedupe\": true," +
				"\n\t\"lead_source_id\": 70000238888," +
				"\n\t\"custom_field\": {" +
				"\n\t\t\"cf_admission_status\": \""+ applyNowGeneralDTO.getAdmissionStatus() +"\"," +
				"\n\t\t\"cf_country_of_study\": \""+applyNowGeneralDTO.getPlaceOfStudy()+"\"," +
				"\n\t\t\"cf_lead_city\": \""+ applyNowGeneralDTO.getCity() +"\"," +
				"\n\t\t\"cf_when_do_you_want_to_study__from_date\": \""+ applyNowGeneralDTO.getTimeOfStudy() +"\",\n\t\t\"cf_lifecycle\": \"Lead\"" +
				",\n\t\t\"cf_contact_status\": \"Open\",\n\t\t\"cf_contact_stage\": \"New Lead\"," +
				"\n\t\t\"cf_contact_sub_status\": \"Captured\",\n\t\t\"cf_source_system\": \"Website\"," +
				"\n\t\t\"cf_sub_lead_source\": \"Avanse Website\",\n\t\t\"cf_sub_lead_source_name\": \""+ applyNowGeneralDTO.getSource() +"\"," +
				"\n\t\t\"cf_partner_type\": null,\n\t\t\"cf_partner_name\": null," +
				"\n\t\t\"cf_loan_amount\": \""+ applyNowGeneralDTO.getLoanAmount() +"\",\n\t\t\"cf_portfolio\": \"Education Loans\"," +
				"\n\t\t\"cf_whatsapp_consent\": \"Yes\",\n\t\t\"cf_whatsapp_consent_date\": \"12/04/2022\"," +
				"\n\t\t\"cf_whastapp_consent_source\": \"Website\"," +
				"\n\t\t\"cf_loan_facility\": \"EL\",\n\t\t\"cf_campaign_id\": \""+applyNowGeneralDTO.getCampaignid() +"\"," +
				"\n\t\t\"cf_googleclick_id\": \""+ applyNowGeneralDTO.getGclid() +"\"\n\t}\n}";
		RequestBody body = RequestBody.create(mediaType, content);

		Request request = new Request.Builder()
				.url("https://fw-avanse.agiratech.com/api/v1/contacts")
				.method("POST", body)
				.addHeader("Authorization", "Basic ZUxPaVM5WEdPbUgwMU5PaFdKcmlhUT09Olg=")
				.addHeader("Content-Type", "application/json")
				.build();
		Response response = client.newCall(request).execute();
		String Result = response.body().string();
		String jsonString =Result;

		//System.out.println("result : " + Result);

		JSONObject obj = new JSONObject(jsonString);
		//String message = obj.getJSONObject(jsonString).getString("contact");
		boolean create = true;
		if(obj.has("create"))
			create = obj.getBoolean("create");


		String message = create ? "" : obj.getString("message");

		System.out.println(message);
		String contacts = obj.has("contact")?obj.getString("contact"):"";

		System.out.println("contacts" + contacts);



		applyNowGeneral.setCrmrequest(content);

		if(!create) {
			applyNowGeneral.setStatus("Rejected");
			applyNowGeneral.setCrmstatus(message);
		} else {
			applyNowGeneral.setStatus("Accepted");
			applyNowGeneral.setLeadid(contacts);
		}

		//applyNowGeneral = applyNowGeneralRepository.getById(45);
		//ApplyNowGeneral applyNowGeneral1 = applyNowGeneralRepository.save(applyNowGeneral);

		//applyNowGeneral.setcrmstatus("");
		//applyNowGeneral.setcrmstatus(applyNowGeneralDTO.getCrmstatus(message));
		applyNowGeneralService.addApplyNowGeneral(applyNowGeneral);

		return "dynamicPages/thankyou";
	}


	
	@PostMapping("/apply-for-e-tutoring/thankyou")
	public String eTutoringAddPost(@ModelAttribute("eTutoringDTO") ETutoringDTO eTutoringDTO) {
		ETutoring eTutoring = new ETutoring();
		eTutoring.setFirstName(eTutoringDTO.getFirstName());
		eTutoring.setLastName(eTutoringDTO.getLastName());
		eTutoring.setCity(eTutoringDTO.getCity());
		eTutoring.setCourseName(eTutoringDTO.getCourseName());
		eTutoring.setCourseProvider(eTutoringDTO.getCourseProvider());
		eTutoring.setEmail(eTutoringDTO.getEmail());
		eTutoring.setLoanAmount(eTutoringDTO.getLoanAmount());
		eTutoring.setPhoneNumber(eTutoringDTO.getPhoneNumber());
		eTutoringService.addETutoring(eTutoring);
		
		return "dynamicPages/thankyou";
	}
	
	@PostMapping("/executive-education-loan/thankyou")
	public String executiveEducationLoanAddPost(@ModelAttribute("executiveEducationLoanDTO") ExecutiveEducationLoanDTO executiveEducationLoanDTO) {
		ExecutiveEducationLoan executiveEducationLoan = new ExecutiveEducationLoan();
		executiveEducationLoan.setFirstName(executiveEducationLoanDTO.getFirstName());
		executiveEducationLoan.setLastName(executiveEducationLoanDTO.getLastName());
		executiveEducationLoan.setEmail(executiveEducationLoanDTO.getEmail());
		executiveEducationLoan.setPhoneNumber(executiveEducationLoanDTO.getPhoneNumber());
		executiveEducationLoan.setCourseName(executiveEducationLoanDTO.getCourseName());
		executiveEducationLoan.setLoanAmount(executiveEducationLoanDTO.getLoanAmount());
		executiveEducationLoanService.addExecutiveEducationLoan(executiveEducationLoan);
		return "dynamicPages/thankyou";
	}
	@PostMapping("/education-institution-loan/thankyou")
	public String educationInstitutionLoanAddPost(@ModelAttribute("educationInstitutionLoanDTO") EducationInstitutionLoanDTO educationInstitutionLoanDTO) {
		EducationInstitutionLoan educationInstitutionLoan= new EducationInstitutionLoan();
		educationInstitutionLoan.setFirstName(educationInstitutionLoanDTO.getFirstName());
		educationInstitutionLoan.setLastName(educationInstitutionLoanDTO.getLastName());
		educationInstitutionLoan.setPhoneNumber(educationInstitutionLoanDTO.getPhoneNumber());
		educationInstitutionLoan.setEmail(educationInstitutionLoanDTO.getEmail());
		educationInstitutionLoan.setCity(educationInstitutionLoanDTO.getCity());
		educationInstitutionLoan.setLoanType(educationInstitutionLoanDTO.getLoanType());
		educationInstitutionLoan.setLoanAmount(educationInstitutionLoanDTO.getLoanAmount());
		educationInstitutionLoan.setLoanUsage(educationInstitutionLoanDTO.getLoanUsage());
		educationInstitutionLoan.setInstitutionType(educationInstitutionLoanDTO.getInstitutionType());
		educationInstitutionLoan.setCurriculumBoard(educationInstitutionLoanDTO.getCurriculumBoard());
		educationInstitutionLoan.setVintage(educationInstitutionLoanDTO.getVintage());
		educationInstitutionLoan.setStudentStrength(educationInstitutionLoanDTO.getStudentStrength());
		educationInstitutionLoanService.addEducationInstitutionLoan(educationInstitutionLoan);
		
//		executiveEducationLoanService.addExecutiveEducationLoan(executiveEducationLoan);
		return "dynamicPages/thankyou";
	}
	
	
	@PostMapping("/apply-for-partner-with-us/thankyou")
	public String partnerWithUssAddPost(@ModelAttribute("partnerWithUsDTO") PartnerWithUsDTO partnerWithUsDTO) {
		
		PartnerWithUs partnerWithUs = new PartnerWithUs();
		partnerWithUs.setFirstName(partnerWithUsDTO.getFirstName());
		partnerWithUs.setLastName(partnerWithUsDTO.getLastName());
		partnerWithUs.setEmail(partnerWithUsDTO.getEmail());
		partnerWithUs.setCity(partnerWithUsDTO.getCity());
		partnerWithUs.setMobileNumber(partnerWithUsDTO.getMobileNumber());
		partnerWithUs.setNameOfFirm(partnerWithUsDTO.getNameOfFirm());
		partnerWithUs.setPartnerType(partnerWithUsDTO.getPartnerType());
		
		partnerWithUsService.addPartnerWithUsLead(partnerWithUs);
		return "dynamicPages/thankyou";
		
	}
	
	@PostMapping("/apply-for-school-fee-financing/thankyou")
	public String schoolFeeFinaningAddPost(@ModelAttribute("schoolFeeFinancingDTO") SchoolFeeFinancingDTO schoolFeeFinancingDTO ) {
		SchoolFeeFinancing schoolFeeFinancing = new SchoolFeeFinancing();
		schoolFeeFinancing.setFirstName(schoolFeeFinancingDTO.getFirstName());
		schoolFeeFinancing.setLastName(schoolFeeFinancingDTO.getLastName());
		schoolFeeFinancing.setCity(schoolFeeFinancingDTO.getCity());
		schoolFeeFinancing.setEmail(schoolFeeFinancingDTO.getEmail());
		schoolFeeFinancing.setLoanAmount(schoolFeeFinancingDTO.getLoanAmount());
		schoolFeeFinancing.setPhoneNumber(schoolFeeFinancingDTO.getPhoneNumber());
		schoolFeeFinancing.setNameOfTheSchool(schoolFeeFinancingDTO.getNameOfTheSchool());
		schoolFeeFinancingService.addSchoolFeeFinancingLead(schoolFeeFinancing);
		return "dynamicPages/thankyou";
		
	}
	
	@PostMapping("/apply-for-skill-enhancement/thankyou")
	public String skillEnhancementAddPost(@ModelAttribute("skillEnhancementDTO") SkillEnhancementDTO skillEnhancementDTO) {
		SkillEnhancement skillEnhancement = new SkillEnhancement();
		skillEnhancement.setCity(skillEnhancementDTO.getCity());
		skillEnhancement.setCourseName(skillEnhancementDTO.getCourseName());
		skillEnhancement.setCourseProvider(skillEnhancementDTO.getCourseProvider());
		skillEnhancement.setEmail(skillEnhancementDTO.getEmail());
		skillEnhancement.setFirstName(skillEnhancementDTO.getFirstName());
		skillEnhancement.setLastName(skillEnhancementDTO.getLastName());
		skillEnhancement.setLoanAmount(skillEnhancementDTO.getLoanAmount());
		skillEnhancement.setPhoneNumber(skillEnhancementDTO.getPhoneNumber());
		skillEnhancementService.addSkillEnhancement(skillEnhancement);
		return "dynamicPages/thankyou";
		
	}
	
	/*
	 * For admin data display
	*/
	
	@GetMapping("/admin/apply-now/general")
	public String applyNowGeneralGet(Model model) {
		model.addAttribute("applyNowGeneralLeads", applyNowGeneralService.getAllApplyNowGeneralLeads());
//		model.addAttribute("university", new University());
		return "apply-now-general";
	} 
	
	
	@GetMapping("/admin/apply-now/e-tutoring")
	public String applyNowETutoringGet(Model model) {
		model.addAttribute("eTutoringLeads", eTutoringService.getAllETutoringLeads());
//		model.addAttribute("university", new University());
		return "apply-now-e-Tutoring";
	} 
	@GetMapping("/admin/apply-now/partner-with-us")
	public String applyNowPartnerWithUsGet(Model model) {
		model.addAttribute("partnerWithUsLeads",  partnerWithUsService.getPartnerWithUsLeads());
//		model.addAttribute("university", new University());
		return "apply-now-partner-with-us";
	} 
	
	@GetMapping("/admin/apply-now/school-fee-financing")
	public String applyNowSchoolFeeFinancingGet(Model model) {
		model.addAttribute("schoolFeeFinancingLeads",  schoolFeeFinancingService.getSchoolFeeFinancingLeads());
//		model.addAttribute("university", new University());
		return "apply-now-school-fee-financing";
	} 
	
	
	@GetMapping("/admin/apply-now/skill-enhancement")
	public String applyNowSkillEnhancementGet(Model model) {
		model.addAttribute("skillEnhancementLeads",  skillEnhancementService.getAllSkillEnhancementsLeads());
		return "apply-now-skill-enhancement";
	} 
	
	
	@GetMapping("/admin/apply-now/education-institution-loan")
	public String applyNowEducationInstitutionLoanGet(Model model) {
		model.addAttribute("educationInstitutionLoanLeads",  educationInstitutionLoanService.getAllEducationInstitutionLoanLeads());
		return "apply-now-education-institution-loan";
	} 
	@GetMapping("/admin/apply-now/executive-education-loan")
	public String applyNowExecutiveEducationLoanGet(Model model) {
		model.addAttribute("executiveEducationLoanLeads", executiveEducationLoanService.getAllExecutiveEducationLoanLeads());
		return "apply-now-executive-education-loan";
	} 
	
	

	
}
