/*
 * This class is going to be a general controller to controll the page linking...
 * 
*/

package com.avanse.springboot.controller.globalPages;
import java.util.List;

import com.avanse.springboot.DTO.forms.contactUs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.avanse.springboot.DTO.SearchResultDTO;
import com.avanse.springboot.DTO.forms.applyNow.ApplyNowGeneralDTO;
import com.avanse.springboot.DTO.forms.applyNow.ETutoringDTO;
import com.avanse.springboot.DTO.forms.applyNow.EducationInstitutionLoanDTO;
import com.avanse.springboot.DTO.forms.applyNow.ExecutiveEducationLoanDTO;
import com.avanse.springboot.DTO.forms.applyNow.PartnerWithUsDTO;
import com.avanse.springboot.DTO.forms.applyNow.SchoolFeeFinancingDTO;
import com.avanse.springboot.DTO.forms.applyNow.SkillEnhancementDTO;
import com.avanse.springboot.DTO.forms.miscellaneous.CSRLeadsDTO;
import com.avanse.springboot.DTO.forms.miscellaneous.CrossSellDTO;
import com.avanse.springboot.DTO.forms.miscellaneous.ENachDTO;
import com.avanse.springboot.DTO.forms.miscellaneous.MoratoriumDeregisterDTO;
import com.avanse.springboot.DTO.forms.miscellaneous.MoratoriumEILDTO;
import com.avanse.springboot.DTO.forms.miscellaneous.MoratoriumFacilityDTO;
import com.avanse.springboot.DTO.forms.miscellaneous.RestructuringOfLoansDTO;
import com.avanse.springboot.DTO.forms.miscellaneous.UnsubscribeDTO;
import com.avanse.springboot.service.AwardService;
import com.avanse.springboot.service.CourseService;
import com.avanse.springboot.service.PageService;
import com.avanse.springboot.service.PostCategoryService;
import com.avanse.springboot.service.PostService;
import com.avanse.springboot.service.SearchService;
import com.avanse.springboot.service.TestimonialService;
import com.avanse.springboot.service.UniversityService;

@Controller

public class PageController {
	
	@Autowired
	PageService pageService;
	
	@Autowired
	PostCategoryService postCategoryService;
	
	@Autowired
	PostService postService;
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	UniversityService universityService;
	
	@Autowired
	AwardService awardService;
	
	@Autowired
	TestimonialService testimonialService;
	
	@Autowired
	SearchService searchService;
	@GetMapping(value={"/robots.txt", "/robot.txt"})
	@ResponseBody
	public String getRobotsTxt() {
	    return "User-agent: *\n" +
				"Disallow: /\n";
	}


	@GetMapping(value={"/index","/"})
	public String homePage(Model model) {
		model.addAttribute("awards", awardService.getAllAwards() );
		model.addAttribute("testimonials", testimonialService.getAllTestimonials());
		return "dynamicPages/index";
	}

	@GetMapping(value={"/index-demo"})
	public String homePageDemo(Model model) {
		model.addAttribute("awards", awardService.getAllAwards() );
		model.addAttribute("testimonials", testimonialService.getAllTestimonials());
		return "dynamicPages/index-demo";
	}


	@GetMapping(value={"/education-loan/study-loan-abroad"})
	public String studyAbroadPage(Model model) {
		
		model.addAttribute("testimonials", testimonialService.getAllTestimonials());

		return "dynamicPages/study-abroad";
	}
	@GetMapping(value={"/education-loan/study-loan-india"})
	public String studyInIndiaPage(Model model) {
		model.addAttribute("testimonials", testimonialService.getAllTestimonials());
		return "dynamicPages/study-in-india";
	}
	
	@GetMapping(value={"/education-loan-for-mba"})
	public String educationLoanForMbaPage(Model model) {
		model.addAttribute("testimonials", testimonialService.getAllTestimonials());
		return "dynamicPages/education-loan-for-mba";
	}
	

	@GetMapping("/search")
	public String searchPage() {
		return "dynamicPages/search";
	}

	@GetMapping("/faqs")
	public String faq() {
		return "addedPages/faq";
	}

	@GetMapping("/gratia-scheme")
	public String gratiaScheme() {
		return "addedPages/gratia-scheme";
	}

	@GetMapping("/education-loan/school-fee-financing")
	public String schoolFeeFinancing() {
		return "addedPages/school-fee-financing";
	}

	@GetMapping("/education-loan/student-loan-refinance")
	public String loanRefinancing() {
		return "addedPages/student-loan-refinancing";
	}

	@GetMapping("/education-loan/e-tutoring-loan")
	public String eTutoringLoan() {
		return "addedPages/e-tutoring-loan";
	}

	@GetMapping("/education-loan/skill-enhancement-loan")
	public String skillEnhancement() {
		return "addedPages/skill-enhancement-loan";
	}

	@GetMapping("/e-tutoring-loan-faq")
	public String eTutoringFaqs() {
		return "addedPages/e-tutoring-loan-faqs";
	}

	@GetMapping("/skill-enhancement-loan-faq")
	public String skillEnhancementFaq() {
		return "addedPages/skill-enhancement-loan-faqs";
	}

	@GetMapping("/school-fee-financing-faq")
	public String schoolFinancingFaqs() {
		return "addedPages/school-fee-financing-faqs";
	}

	@GetMapping("/education-institution-loan-faq")
	public String educationInstitutionFaqs() {
		return "addedPages/education-institution-loan-faq-s";
	}

	@GetMapping("/executive-education-loan-faq")
	public String executiveEducationFaqs() {
		return "addedPages/executive-education-loan-faqs";
	}

	@GetMapping("/education-loan-faq")
	public String educationLoanFaqs() {
		return "addedPages/education-loan-faqs";
	}

	@GetMapping("/study-in-india-faq")
	public String studyIndiaFaqs() {
		return "addedPages/study-in-india-faqs";
	}

	@GetMapping("/study-in-abroad-faq")
	public String studyAbroadFaqs() {
		return "addedPages/study-in-abroad-faqs";
	}

	@GetMapping("/awards")
	public String awardsPage() {
		return "addedPages/awards---recognition";
	}

	@GetMapping("/media-room")
	public String mediaRoom() {
		return "addedPages/media-room";
	}

	@GetMapping("/education-loan-insurance-service")
	public String insuranceServices() {
		return "addedPages/insurance-services";
	}

	@GetMapping("/education-loan-calculators/resources")
	public String resourcesPage() {
		return "addedPages/resources";
	}

	@GetMapping("/country")
	public String countryPage() {
		return "dynamicPages/country";
	}
	
	@PostMapping(value = "/globalSearch/{searchKey}")
	@ResponseBody
	public List<SearchResultDTO> globalSearchAPI(@PathVariable String searchKey){
		return searchService.getGlobalSearchResults(searchKey);
	}

	@GetMapping("/career")
	public String careerPage(Model model) {
		model.addAttribute("testimonials", testimonialService.getAllTestimonials());
		model.addAttribute("awards", awardService.getAllAwards() );
		return "dynamicPages/career";
	}
	@GetMapping("/about-avanse/career/jobsearch")
	public String jobApplyPage() {
		return "dynamicPages/jobsearch";
	}
	@GetMapping("/about-avanse/career/jobdetails")
	public String jobDetailsPage() {
		return "dynamicPages/jobdetails";
	}
	
	@GetMapping("/education-loan-calculators/eligibility-calculator")
	public String educationLoanEligibilityCalculatorPage() {
		return "dynamicPages/calculatorPages/education-loan-eligibility-calculator";
	}
	@GetMapping("/education-loan-calculators/emi-calculator")
	public String educationLoanEMICalculatorPage() {
		return "dynamicPages/calculatorPages/education-loan-emi-calculator";
	}
	@GetMapping("/education-loan-calculators/repayment-calculator")
	public String educationLoanRepaymentCalculatorPage() {
		return "dynamicPages/calculatorPages/education-loan-repayment-calculator";
	}
	@GetMapping("/education-loan-calculators/college-expense-calculator")
	public String collegeExpenseCalculatorPage() {
		return "dynamicPages/calculatorPages/college-expense-calculator";
	}
	
	@GetMapping("/about")
	public String aboutPage() {
		return "about";
	}

	@GetMapping("/testimonials")
	public String testimonialsPage(Model model) {
		model.addAttribute("testimonials", testimonialService.getAllTestimonials());
		return "dynamicPages/testimonials";
	}
		
	@GetMapping("/courseDetail/{courseId}")
	public String courseDetailsPage(@PathVariable long courseId, Model model) {
		model.addAttribute("course", courseService.getCourseById(courseId).get());
		return "dynamicPages/courseDetail";
	}
	
	@GetMapping("/UID/{universityId}")
	public String universityDetailsPage(@PathVariable long universityId, Model model) {
		model.addAttribute("university", universityService.getUniversityById(universityId).get());
		return "dynamicPages/university";
	}
	
	
	  @GetMapping(value =
	  {"/Country/{country}","/Country/{country}/{courseId}","/Country/{country}/UID={universityId}",
	  "/Country/{country}/{courseId}/{universityId}"}) public String
	  countryPage(@PathVariable("country") String countryName, Model model,
	  @PathVariable(name = "courseId",required = false) Long courseId,
	  @PathVariable(name = "universityId",required = false) Long universityId) {
	  
			/*
			 * //System.out.println("Testing Country --- > "+countryName);
			 * //System.out.println("Testing CourseId --- > "+courseId);
			 * //System.out.println("Testing UniversityID --- > "+universityId);
			 */
	  
	  if(courseId!=null) {
		  model.addAttribute("courseIdCheck", "courseIdIsPresent");
		  model.addAttribute("incomingCourseModel", courseService.getCourseById(courseId).get());
	  }
	  if(universityId!=null) {
		  model.addAttribute("universityIdCheck", "universityIdIsPresent");
		  model.addAttribute("incomingUniversityModel", universityService.getUniversityById(universityId).get());
	  }
	  
	  return "dynamicPages/"+countryName; 
	  }
	 
	@GetMapping("/admin/viewPages/{extractedFileName}")
	public ModelAndView getAddedPage(@PathVariable("extractedFileName") String extractedFileName) {
		ModelAndView modelAndView = new ModelAndView("addedPages/"+extractedFileName);	
		return modelAndView;		
	}
	
	@GetMapping("/blog")
	public ModelAndView getBlogPage(Model model, 
			@RequestParam(value = "searchKey",required = false) String searchKey) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/blog");
//		//System.out.println("testing.........searchkey : "+searchKey);
		model.addAttribute("postCategories", postCategoryService.getAllPostCategories());
		model.addAttribute("posts", postService.getTopFourLatestPosts());
		model.addAttribute("searchKey", searchKey);
		return modelAndView;
	}
	
	@GetMapping("/contact-us")
	public ModelAndView getContactUsCustomer(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/contactus");
		model.addAttribute("customerDTO", new CustomerDTO());
		model.addAttribute("instituteDTO", new InstituteDTO());
		model.addAttribute("investorDTO", new InvestorDTO());
		model.addAttribute("mediaDTO", new MediaDTO());
		model.addAttribute("associateWithUsDTO", new AssociateWithUsDTO());
		return modelAndView;
	}
	
	  @GetMapping("/apply-now")

	  public ModelAndView getApplyNowForm(Model model) {
	  ModelAndView modelAndView = new ModelAndView("dynamicPages/apply-now");
	  model.addAttribute("applyNowGeneralDTO", new ApplyNowGeneralDTO()); 
	  model.addAttribute("awards", awardService.getAllAwards() );
	  return modelAndView;  
	  }
	@GetMapping("/apply-now-new")

	public ModelAndView getApplyNowNewForm(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/apply-now-new");
		model.addAttribute("applyNowGeneralDTO", new ApplyNowGeneralDTO());
		model.addAttribute("testimonials", testimonialService.getAllTestimonials());
		model.addAttribute("awards", awardService.getAllAwards() );
		return modelAndView;
	}
	@GetMapping("/loan-apply")

	public ModelAndView getLoanApplyForm(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/loan-apply");
		model.addAttribute("applyNowGeneralDTO", new ApplyNowGeneralDTO());
		model.addAttribute("awards", awardService.getAllAwards() );
		model.addAttribute("testimonials", testimonialService.getAllTestimonials());
		return modelAndView;
	}
	 
	@GetMapping("/education-institution-loan")
	public ModelAndView getEducationInstitutionLoan(Model model) {
		ModelAndView modelAndView =  new ModelAndView("addedPages/education-institution-loans");
		model.addAttribute("educationInstitutionLoanDTO", new EducationInstitutionLoanDTO());
		model.addAttribute("awards", awardService.getAllAwards() );
		return modelAndView;	
	}
	
	@GetMapping("/apply-for-executive-education-loan")
	public ModelAndView getExecutiveEducationLoan(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/executive-education-loan");
		model.addAttribute("executiveEducationLoanDTO", new ExecutiveEducationLoanDTO());
		model.addAttribute("awards", awardService.getAllAwards() );
		return modelAndView;
	}
	
	@GetMapping("/apply-for-e-tutoring")
	public ModelAndView getApplyForEtutoringPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/apply-for-e-tutoring");
		model.addAttribute("eTutoringDTO", new ETutoringDTO());
		model.addAttribute("awards", awardService.getAllAwards());
		return modelAndView;
	}
	
	
	@GetMapping("/apply-for-partner-with-us")
	public ModelAndView getPartnerWithUsPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/apply-for-partner-with-us");
		model.addAttribute("partnerWithUsDTO", new PartnerWithUsDTO());
		model.addAttribute("awards", awardService.getAllAwards() );

		return modelAndView;
	}
	
	@GetMapping("/apply-for-school-fee-financing")
	public ModelAndView getSchoolFeeFinancingPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/apply-for-school-fee-financing");
		model.addAttribute("schoolFeeFinancingDTO", new SchoolFeeFinancingDTO());
		model.addAttribute("awards", awardService.getAllAwards());
		return modelAndView;
	}

	@GetMapping("/apply-for-skill-enhancement")
	public ModelAndView getApplyForSkillEnhancementPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/apply-for-skill-enhancement");
		model.addAttribute("skillEnhancementDTO", new SkillEnhancementDTO());
		model.addAttribute("awards", awardService.getAllAwards());
		return modelAndView;
	}
	
	@GetMapping("/associate-with-us")
	public ModelAndView getAssociateWithUsPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/associate-with-us");
		model.addAttribute("associateWithUsDTO", new AssociateWithUsDTO());
		return modelAndView;
	}
	
	@GetMapping("/corporate-social-responsibility")
	public ModelAndView getCSRPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/csr");
		model.addAttribute("csrLeadsDTO", new CSRLeadsDTO());
		return modelAndView;
	}
	@GetMapping("/e-nach")
	public ModelAndView getENachPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/e-nach");
		model.addAttribute("eNachDTO", new ENachDTO());
		return modelAndView;
	}

	@GetMapping("/restructuring-of-loans-step1")
	public ModelAndView getRestructuringOfLoansPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/restructuring-of-loans");
		model.addAttribute("restructuringOfLoansDTO", new RestructuringOfLoansDTO());
		return modelAndView;
	}
	
	@GetMapping("/moratorium-facility-2020")
	public ModelAndView getMoratoriumFacilityPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/moratorium-facility-2020");
		model.addAttribute("moratoriumFacilityDTO", new MoratoriumFacilityDTO());
		return modelAndView;
	}

	@GetMapping("/moratorium-facility-deregister")
	public ModelAndView getMoratoriumFacilityDeregisterPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/moratorium-facility-deregister");
		model.addAttribute("moratoriumDeregisterDTO", new MoratoriumDeregisterDTO());
		return modelAndView;
	}
	
	@GetMapping("/cross-sell")
	public ModelAndView crossSellPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/cross-sell");
		model.addAttribute("crossSellDTO", new CrossSellDTO());
		return modelAndView;
	}
	
	@GetMapping("/moratorium-eil")
	public ModelAndView moratoriumEILPage(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/moratorium-eil");
		model.addAttribute("moratoriumEILDTO", new MoratoriumEILDTO());
		return modelAndView;
	}
	
	@GetMapping("/unsubscribe")
	public ModelAndView getUnsubscribePage(Model model) {
		ModelAndView modelAndView = new ModelAndView("dynamicPages/unsubscribe");
		model.addAttribute("unsubscribeDTO", new UnsubscribeDTO());
		return modelAndView;
	}


	/*
	 * Page for all forms
	*/
	
}
