package com.avanse.springboot.controller;

import com.avanse.springboot.DTO.*;
import com.avanse.springboot.FileUtils;
import com.avanse.springboot.model.*;
import com.avanse.springboot.model.forms.contactUs.Customer;
import com.avanse.springboot.model.forms.contactUs.Institute;
import com.avanse.springboot.model.forms.contactUs.Media;
import com.avanse.springboot.model.forms.contactUs.exports.CustomersCSVExporter;
import com.avanse.springboot.model.forms.contactUs.exports.InstitutesCSVExporter;
import com.avanse.springboot.model.forms.contactUs.exports.MediaLeadsCSVExporter;
import com.avanse.springboot.repository.*;
import com.avanse.springboot.service.*;
import com.avanse.springboot.service.forms.contactUs.CustomerService;
import com.avanse.springboot.service.forms.contactUs.InstituteService;
import com.avanse.springboot.service.forms.contactUs.MediaService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Controller
public class AdminController {

	@Autowired
	private Environment env;

	@Autowired
	private FileUtils fileUtils;

	@Autowired
	AwardRepository awardRepository;

	@Autowired
	AwardService awardService;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	CourseService courseService;

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	ImageService imageService;

	@Autowired
	JobRespository jobRespository;

	@Autowired
	JobService jobService;

	@Autowired
	LocationRepository locationRepository;

	@Autowired
	LocationService locationService;

	@Autowired
	PageRepository pageRepository;

	@Autowired
	PageService pageService;

	@Autowired
	PostRepository postRepository;

	@Autowired
	PostService postService;

	@Autowired
	PostCategoryRepository postCategoryRepository;

	@Autowired
	PostCategoryService postCategoryService;

	@Autowired
	UniversityRepository universityRepository;

	@Autowired
	UniversityService universityService;

	@Autowired
	TestimonialRepository testimonialRepository;

	@Autowired
	TestimonialService testimonialService;

	@Autowired
	HeaderRepository headerRepository;

	@Autowired
	HeaderService headerService;

	@Autowired
	TypeDetailsCourseRepository typeDetailsCourseRepository;

	@Autowired
	TypeDetailsCourseService typeDetailsCourseService;

	@LocalServerPort
	int activePortNumber;

	/*
	 * Method to show the admin home page.
	 */
	@GetMapping("/admin")
	public String adminDashboard(Model model) {

		Long noOfUniversities = universityService.numberOfUniversities();
		Long noOfCourses = courseService.numberOfCourses();
		Long noOfPages = pageService.numberOfPages();
		Long noOfPosts = postService.numberOfPosts();
//		//System.out.println("Number of University is " + noOfUniversities);
		model.addAttribute("numOfUniversities", noOfUniversities);
		model.addAttribute("numOfCourses", noOfCourses);
		model.addAttribute("numOfPages", noOfPages);
		model.addAttribute("numOfPosts", noOfPosts);
//		//System.out.println("User Added Image Directory is " + fileUtils.getResourcePath("resources.assets.userAddedImagesDir"));
		return "adminDashboard";

	}

	@GetMapping("/admin/manage")
	public String adminHome() {
		return "adminHome";
	}

	/*
	 * Method to add a awards Need both get and post mapping for adding the
	 * university because the request could be of any type...
	 */

	@GetMapping("/admin/awards")
	public String getAwards(Model model) {
//		model.addAttribute("awards", awardService.getAllAwards());
		return listAwardsByPage(1, model);
	}

	@GetMapping("/admin/awards/page/{pageNum}")
	public String listAwardsByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
		org.springframework.data.domain.Page<Award> page = awardService.listAwardsByPage(pageNum);

		List<Award> awards = page.getContent();
//
//		//System.out.println("PageNum =" + pageNum);
//		//System.out.println("Total elements= " + page.getNumberOfElements());
//		//System.out.println("Total Pages= " + page.getTotalPages());

		long startCount = (pageNum - 1) * awardService.AWARDS_PER_PAGE + 1;
		long endCount = startCount + awardService.AWARDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("awards", awards);
		return "awards";

	}

	@GetMapping("/admin/awards/add")
	public String awardsAddGet(Model model) {
		model.addAttribute("awardDTO", new AwardDTO());
		return "awardsAdd";
	}

	@PostMapping("/admin/awards/add")
	public String awardsAddPost(@ModelAttribute("awardDTO") AwardDTO awardDTO) {
		Award award = new Award();
		award.setId(awardDTO.getId());
		award.setTitle(awardDTO.getTitle());
		award.setDescription(awardDTO.getDescription());
		awardService.addAward(award);
		return "redirect:/admin/awards";
	}

	/*
	 * Function to delete an award by id
	 */
	@GetMapping("/admin/award/delete/{id}")
	public String deleteAward(@PathVariable long id) {
		if (awardRepository.findById(id).isPresent()) {
			awardService.deleteAward(id);
		}

		else {
//			//System.out.println("Cannot Delete the object, Later to be displayed over the page");
		}
		return "redirect:/admin/awards";
	}

	@GetMapping("/admin/award/update/{id}")
	public String updateAward(@PathVariable long id, Model model) {
		Award award = awardService.getAwardById(id).get();
		AwardDTO awardDTO = new AwardDTO();
		awardDTO.setId(award.getId());
		awardDTO.setTitle(award.getTitle());
		awardDTO.setDescription(award.getDescription());

		model.addAttribute("awardDTO", awardDTO);
		return "awardsAdd";
	}

	/*
	 * ===================All methods below are related to universities
	 * ================================================
	 */

	/*
	 * Method to show the universities.html
	 */
	@GetMapping("/admin/universities")
	public String getFirstUniversityPage(Model model) {

//		List<University>universities = universityService.getAllUniversity();
		model.addAttribute("universities", universityService.getAllUniversity());

		return "universities";
	}

	/*
	 * Method to add a university Need both get and post mapping for adding the
	 * university because the request could be of any type...
	 */
	@GetMapping("/admin/universities/add")
	public String universitiesAddGet(Model model) {
		model.addAttribute("universityDTO", new UniversityDTO());
//		model.addAttribute("university", new University());
		return "universitiesAdd";
	}

	/*
	 * On writing the post mapping you will be able to upload images to the server.
	 */
	@PostMapping("/admin/universities/add")
	public String universitiesAddPost(@ModelAttribute("universityDTO") UniversityDTO universityDTO,
			@RequestParam("universityImage") MultipartFile file, @RequestParam("imgName") String imgName,
			@RequestParam(value = "summer", required = false) String summer,
			@RequestParam(value = "winter", required = false) String winter,
			@RequestParam(value = "fall", required = false) String fall,
			@RequestParam(value = "spring", required = false) String spring) throws IOException {
//		universityService.addUniversity(university);

		/*
		 * Creating a new university object and a university dto object so that we can
		 * transfer the data from the dto to the main university model.
		 */
		University university = new University();

		/*
		 * Just after the university object is create, initialise the date object and
		 * then get the milliseconds in long Now convert this milliseconds in the String
		 * format and store it in the variable which can be used further
		 */
		Date date = new Date();

		/*
		 * Get the milliseconds using the date object since 1970
		 */

		long millisecsFrom1970 = date.getTime();

		/*
		 * Convert the millisecs to String that can be pushed into the database
		 */

		String modifiedFileNameByDate = String.valueOf(millisecsFrom1970);

		/*
		 * Passing data into the main university
		 */
		university.setId(universityDTO.getId());
		university.setName(universityDTO.getName());
		university.setLocation(universityDTO.getLocation());
		university.setEstablishedYear(universityDTO.getEstablishedYear());
		university.setIntakePeriod(universityDTO.getIntakePeriod());
		university.setAccomodation(universityDTO.getAccomodation());
		university.setApplicationProcess(universityDTO.getApplicationProcess());
		university.setDescription(universityDTO.getDescription());
		university.setImageName(universityDTO.getImageName());
		university.setStaticContent(universityDTO.getStaticContent());

		if (summer != null || winter != null || fall != null || spring != null) {

			String insummer = "";
			if (summer != null)
				insummer = "SummerIntake-" + summer;
			String inwinter = "";
			if (winter != null)
				inwinter = "WinterIntake-" + winter;
			String infall = "";
			if (fall != null)
				infall = "FallIntake-" + fall;
			String inspring = "";
			if (spring != null)
				inspring = "SpringIntake-" + spring;
			String finalIntakeString = insummer + "," + inwinter + "," + infall + "," + inspring;
			int i=0;
			while (finalIntakeString.endsWith(",")) {
				finalIntakeString = finalIntakeString.substring(0, finalIntakeString.length() - 1	);
			}
			i=0;
			while (finalIntakeString.startsWith(",")) {
				finalIntakeString = finalIntakeString.substring(1);
			}
			i=0;
			while (finalIntakeString.matches(".*?,,+.*")) {
				finalIntakeString = finalIntakeString.replaceAll("[,][,]", ",");
			}

			university.setIntakePeriod(finalIntakeString);
		}
		/*
		 * Create the imageUUID and using the nio package get the filename and the path
		 */
		String imageUUID;
		if (!file.isEmpty()) {

			/*
			 * Extract The file extention using get Extention method and store in the ext
			 * variable.
			 */
			String ext = FilenameUtils.getExtension(file.getOriginalFilename());

			/*
			 * Generate the complete file name with extention
			 */
			imageUUID = modifiedFileNameByDate + '.' + ext;

			/*
			 * Use the nio library to do the stream operations. Paas the universal Unique ID
			 * and the university upload Directory
			 */
			Path fileNameAndPath = Paths.get(fileUtils.getResourcePath("resources.images.universityUploadDir"), imageUUID);
			Files.write(fileNameAndPath, file.getBytes());

		} else {
			imageUUID = imgName;

		}

		/*
		 * Pass the UUID into the imagename of the university...
		 */
		university.setImageName(imageUUID);

		/*
		 * Use the university Service to finally add and save the university.
		 */
		universityService.addUniversity(university);

		return "redirect:/admin/universities";
	}

	/*
	 * Function to delete a university by id
	 */
	@GetMapping("/admin/university/delete/{id}")
	public String deleteUniversity(@PathVariable long id) {

		/*
		 * Before deleting the object, check if the university exist or not and for that
		 * you will need university repository.
		 *
		 * Calling the delete Image function before this record can be deleted from the
		 * database, otherwise we will never be able to access its name in future
		 *
		 */
		List<Course> coursesByUniversityId = courseService.getCoursesByUniversityId(id);
		if(coursesByUniversityId != null && !coursesByUniversityId.isEmpty()) {
			for(Course course : coursesByUniversityId) {
				courseService.deleteCourse(course.getId());
			}
		}

		if (universityRepository.findById(id).isPresent()) {
			deleteImageFromStaticFolder(id);
			universityService.removeUniversityById(id);
		}

		else {
			//System.out.println("Cannot Delete the object, Later to be displayed over the page");
		}

		return "redirect:/admin/universities";
	}

	/*
	 * Function to delete the image from the server before it can be deleted from
	 * the database...
	 */

	public void deleteImageFromStaticFolder(@PathVariable long id) {
		University universityImageToBeDeleted = universityService.getUniversityById(id).get();
//		UniversityDTO universityDTO = new UniversityDTO();
		String myFile = universityImageToBeDeleted.getImageName();
		/*
		 * Give the exact path where the file is located followed by a slash and then
		 * use the service method of get University by ID
		 */
		File file = new File(fileUtils.getResourcePath("resources.images.universityUploadDir") + File.separator + myFile);
//		//System.out.println(file.getAbsolutePath());

		/*
		 * Check if the file exist before deleting and after deleting
		 */
		if (file.exists())
			file.delete();
	}

	/*
	 * Here I have defined the update method. For universities we have to set the
	 * dto and pass the model
	 */

	@GetMapping("/admin/university/update/{id}")
	public String updateUniversity(@PathVariable long id, Model model) {
		University university = universityService.getUniversityById(id).get();
		UniversityDTO universityDTO = new UniversityDTO();

		universityDTO.setId(university.getId());
		universityDTO.setName(university.getName());
		universityDTO.setLocation(university.getLocation());
		universityDTO.setEstablishedYear(university.getEstablishedYear());
		universityDTO.setIntakePeriod(university.getIntakePeriod());
		universityDTO.setAccomodation(university.getAccomodation());
		universityDTO.setApplicationProcess(university.getApplicationProcess());
		universityDTO.setDescription(university.getDescription());
		universityDTO.setImageName(university.getImageName());
		universityDTO.setStaticContent(university.getStaticContent());

		model.addAttribute("universityDTO", universityDTO);
		model.addAttribute("updateUniversityCheck", "true");
		return "universitiesAdd";
	}

	public void preLoadUniversity(Model model) {
		List<University> myUniversityList = universityService.getAllUniversity();
		model.addAttribute("universityList", myUniversityList);
	}

	// Activate Deactivate university
	@GetMapping("/admin/activateDeactivateUniversity/{id}/{action}")
	@ResponseBody
	@CrossOrigin("*")
	public String activateDeactivateUniversity(@PathVariable(name = "id") long id, @PathVariable String action) {
		//System.out.println("Requested for University action = " + action + " for University id= " + id);
		if (action.equals("ActivateUniversity")) {
			University university = universityService.getUniversityById(id).get();
			university.setIsUniversityActive(true);
			universityService.addUniversity(university);
			return "University Activated!!";
		} else {
			University university = universityService.getUniversityById(id).get();
			university.setIsUniversityActive(false);
			universityService.addUniversity(university);
			return "University De-Activated!!";
		}
	}

	/*
	 * ========================================== All Function below are related to
	 * courses ===========================================
	 */

	/*
	 * Function to show the courses
	 */

	@GetMapping("/admin/courses")
	public String getFirstCoursePage(Model model) {

		List<University> universities = universityService.getAllUniversity();
		model.addAttribute("universities", universityService.getAllUniversity());
		model.addAttribute("courses", courseService.getAllCourses());
		return "courses";
	}

	/*
	 * Function to add a course Again we will need to create both get and post
	 * mapping
	 *
	 */

	@GetMapping("/admin/courses/add")
	public String coursesAddGet(Model model) {
		model.addAttribute("courseDTO", new CourseDTO());
//		Course course = new Course();
		/*
		 * Pass the university list to the dropdown on courseAdd.html
		 */
		University university = new University();
		model.addAttribute("university", university);
		List<University> universities = universityService.getAllUniversity();
		//System.out.println(universities.toString());
		model.addAttribute("universities", universities);

		/*
		 * Now use the course service to actually add the object
		 */

//		courseService.addCourse(course);

		return "coursesAdd";
	}

	/*
	 * Here implementing the post mapping for courses add
	 */

	@PostMapping("/admin/courses/add")
	public String coursesAddPost(@ModelAttribute("courseDTO") CourseDTO courseDTO,
			@RequestParam("university_id") long university_id, @RequestParam(required = false,name="typeCheckBox") String[] courseTypes,
			@RequestParam(required = false,name="examCheckBox") String[] exams, @RequestParam(required = false,name="documentsRequired") String[] documentsRequired,
			@RequestParam(required = false,name="academicDocumentsRequired") String[] academicDocumentsRequired) {

		/*
		 * Use the model attribute to transfer the data from course DTO to course object
		 */

		String examsFromCheckBoxes = "";

		if (exams != null) {
			for (int i= 0; i<exams.length;i++) {

				if(i==exams.length-1) {
				examsFromCheckBoxes += exams[i];
				}

				else {
					examsFromCheckBoxes+=exams[i]+",";
				}
			}

		}
		courseDTO.setExamsEligibility(examsFromCheckBoxes);

//		University university = new University();
		Course course = new Course();
		course.setId(courseDTO.getId());
		course.setTitle(courseDTO.getTitle());
		course.setDescription(courseDTO.getDescription());
		course.setDuration(courseDTO.getDuration());
		course.setMonth(courseDTO.getMonth());
//		course.setDocumentsRequired(courseDTO.getDocumentsRequired());

		String documentsRequiredCheckboxes = "";

		if (documentsRequired != null) {
			for (int i = 0; i < documentsRequired.length; i++) {

				if (i == documentsRequired.length - 1) {
					documentsRequiredCheckboxes += documentsRequired[i];
				} else {
					documentsRequiredCheckboxes += documentsRequired[i] + ",";

				}
			}
		}

		courseDTO.setDocumentsRequired(documentsRequiredCheckboxes);
		course.setDocumentsRequired(courseDTO.getDocumentsRequired());

		String academicDocumentsRequiredCheckBoxes = "";

		if (academicDocumentsRequired != null) {
			for (int i = 0; i < academicDocumentsRequired.length; i++) {
				if (i == academicDocumentsRequired.length - 1) {
					academicDocumentsRequiredCheckBoxes += academicDocumentsRequired[i];

				} else {
					academicDocumentsRequiredCheckBoxes += academicDocumentsRequired[i] + ",";
				}
			}
		}

		courseDTO.setAcademicDocumentsRequired(academicDocumentsRequiredCheckBoxes);
		course.setAcademicDocumentsRequired(courseDTO.getAcademicDocumentsRequired());

		course.setExamsEligibility(courseDTO.getExamsEligibility());
		course.setFees(courseDTO.getFees());
		course.setStaticContent(courseDTO.getStaticContent());
		List<TypeDetailsCourse> tList = new ArrayList<>();
		for (String s : courseTypes) {
			TypeDetailsCourse tdc = new TypeDetailsCourse();
			tdc.setName(s);
			typeDetailsCourseService.addTypeDetailsCourse(tdc);
			tList.add(tdc);
		}
		course.setTypes(tList);

//		university.addTheCourse(course);
		course.setUniversity(universityService.getUniversityById(university_id).get());
		courseService.addCourse(course);
		//System.out.println(course.toString());
		return "redirect:/admin/courses";
	}

	/*
	 * Function to delete a course by its id
	 */
	@GetMapping("/admin/course/delete/{id}")
	public String deleteCourse(@PathVariable long id) {

		if(universityService.getUniversityContainingCourseId(id).isPresent()) {
			University university = universityService.getUniversityContainingCourseId(id).get();
			List<Course> courses = university.getCourses();
			int index = -1;
			for(int i=0;i<courses.size();i++) {
				if(courses.get(i).getId() == id) {
					//System.out.println("TESTING .................Index Found - " + i);
					index = i;
				}
			}
			if(index != -1) {
				courses.remove(index);
				//System.out.println("TESTING .................saving university " + university);
				//System.out.println("Courses..............."+university.getCourses());
				universityService.addUniversity(university);
				courseService.deleteCourse(id);
			}

		}

		return "redirect:/admin/courses";
	}

	/*
	 * Method to update a course by its id Set DTO and pass the model as an argument
	 */

	@GetMapping("/admin/course/update/{id}")
	public String updateCourse(@PathVariable long id, Model model) {
		Course course = courseService.getCourseById(id).get();
		CourseDTO courseDTO = new CourseDTO();

		courseDTO.setId(course.getId());
		courseDTO.setTitle(course.getTitle());
		courseDTO.setDescription(course.getDescription());
		courseDTO.setDocumentsRequired(course.getDocumentsRequired());
		courseDTO.setAcademicDocumentsRequired(course.getAcademicDocumentsRequired());
		courseDTO.setDuration(course.getDuration());
		courseDTO.setMonth(course.getMonth());
		courseDTO.setExamsEligibility(course.getExamsEligibility());
		courseDTO.setFees(course.getFees());
		courseDTO.setUniversity(course.getUniversity());
		courseDTO.setStaticContent(course.getStaticContent());
		model.addAttribute("courseDTO", courseDTO);
		model.addAttribute("updateCourseCheck", "true");
		List<TypeDetailsCourse> types = course.getTypes();
		List<String> currentCoursesTypesString = new ArrayList<>();
		for (TypeDetailsCourse cour : types) {
			currentCoursesTypesString.add(cour.getName());
		}
		model.addAttribute("currentCourseTypes", currentCoursesTypesString);

		/*
		 * Pass the university list to the dropdown on courseAdd.html
		 */
		University university = new University();
		model.addAttribute("university", university);
		List<University> universities = universityService.getAllUniversity();
		//System.out.println(universities.toString());
		model.addAttribute("universities", universities);

		/*
		 * Now use the course service to actually add the object
		 */



		return "coursesAdd";
	}

	// Activate Deactivate course
	@GetMapping("/admin/activateDeactivateCourse/{id}/{action}")
	@ResponseBody
	@CrossOrigin("*")
	public String activateDeactivateCourse(@PathVariable(name = "id") long id, @PathVariable String action) {
		//System.out.println("Requested for Course action = " + action + " for Course id= " + id);
		if (action.equals("ActivateCourse")) {
			Course course = courseService.getCourseById(id).get();
			course.setIsCourseActive(true);
			courseService.addCourse(course);
			return "Course Activated!!";
		} else {
			Course course = courseService.getCourseById(id).get();
			course.setIsCourseActive(false);
			courseService.addCourse(course);
			return "Course De-Activated!!";
		}
	}

	/*
	 * ===========All Function below are related to images================
	 */

	/*
	 * Method to show the images.html
	 */

	@GetMapping("/admin/images")
	public String getImages(Model model) {
		model.addAttribute("images", imageService.getAllImages());
		return "images";
	}

	@PostMapping(path = "/admin/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@CrossOrigin("*")
	public ResponseEntity<String> postImages(@RequestParam(name = "imageList") MultipartFile[] imageList) throws InterruptedException {

		System.out.println("Length -    " + imageList.length); // This Upload will work only if this statement is
																// present
		for (MultipartFile mFile : imageList) {
			try {
				Path fileNameAndPath = Paths.get(fileUtils.getResourcePath("resources.images.userAddedImagesDir"), mFile.getOriginalFilename());
				Files.write(fileNameAndPath, mFile.getBytes());
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

//		Review To be Done for image

//		List<Thread> threadListForSavingInDatabase = new ArrayList<Thread>();
		for (MultipartFile mFile : imageList) {
//			Thread t1 = new Thread(() -> {
				try {

					InetAddress inetAddress = InetAddress.getLocalHost();
					Image image = new Image(mFile.getName(), mFile.getOriginalFilename(),

							env.getProperty("resources.images.userAddedImagesPath") + "/" + mFile.getOriginalFilename(), mFile.getName(),
							mFile.getSize() / 1024);
					System.out.println(image);
					imageService.addImage(image);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
//			});

//			threadListForSavingInDatabase.add(t1);
		}
		/*for (Thread t : threadListForSavingInDatabase) {
			t.start();
		}
		for (Thread t : threadListForSavingInDatabase) {
			t.join();
		}*/

//		Thread.sleep(5000);

		return ResponseEntity.ok().body("success");
	}
	/*
	 * @GetMapping("/admin/images/add") public String imagesAddGet(Model model) {
	 * model.addAttribute("imageDTO", new ImageDTO());
	 *
	 * Image image = new Image(); model.addAttribute("image", image); List<Image>
	 * images = imageService.getAllImages(); //System.out.println(images.toString());
	 * model.addAttribute("images", images);
	 *
	 * return "imagesAdd"; }
	 */

	// image delete

	@GetMapping("/admin/image/delete/{id}")
	public String deleteImage(@PathVariable long id) {

		if (imageRepository.findById(id).isPresent()) {
			deleteImageFromUserAddedImagesFolder(id);
			imageService.removeImageById(id);
		}

		else {
			//System.out.println("Cannot Delete the object, Later to be displayed over the page");

		}

		return "redirect:/admin/images";
	}

	private void deleteImageFromUserAddedImagesFolder(@PathVariable long id) {

		Image imageFileToBeDeleted = imageService.getImagebyId(id).get();
		String theFile = imageFileToBeDeleted.getFileName();
		File file = new File(fileUtils.getResourcePath("resources.assets.userAddedImagesDir") + File.separator + theFile);
		if (file.exists())
			file.delete();
	}

	/*
	 * ===========All Function below are related to jobs ================
	 */

	/*
	 * Method to show the jobs.html
	 */
	@GetMapping("/admin/jobs")
	public String getJobs(Model model) {
		model.addAttribute("jobs", jobService.getAllJobs());
		return "jobs";
	}

	/*
	 * Method to add a job. For that we will need both the get and post mapping. Get
	 * mapping to open the form Post mapping to send the data from the form and
	 * process it via controller
	 */

	@GetMapping("/admin/jobs/add")
	public String jobsAddGet(Model model) {
		model.addAttribute("jobDTO", new JobDTO());
		model.addAttribute("locations", locationService.getAllLocations());
		return "jobsAdd";
	}

	@PostMapping(path = "/admin/jobs/add")
	public String jobsAddPost(@ModelAttribute("jobDTO") JobDTO jobDTO,
			@RequestParam("selectedLocations") String[] locationsIds,
			@RequestParam(value = "updateOperation", required = false) String isUpdating) {
		Job job;
		if (jobDTO.getId() == null) {
			job = new Job();
		}

		else {
			job = jobService.getJobById(jobDTO.getId()).get();
		}
		job.setId(jobDTO.getId());
		job.setTitle(jobDTO.getTitle());

		job.setShortDescription(jobDTO.getShortDescription());
		job.setDescription(jobDTO.getDescription());
		job.setPostedBy(jobDTO.getPostedBy());
		job.setExperienceInYears(jobDTO.getExperienceInYears());
		job.setSkills(jobDTO.getSkills());

		String dateOfJobCreated = new SimpleDateFormat("dd MMMM, yyyy").format(new Date());
		job.setJobCreatedDate(dateOfJobCreated);

		List<Job> allJobs = jobRespository.findAll();
		Iterator<Job> iterator = allJobs.iterator();

		jobService.addJob(job);

		if (isUpdating == null) {
			for (String s : locationsIds) {
				Location location = locationRepository.getById(Long.valueOf(s));
				location.getJobs().add(job);
				locationRepository.save(location);
			}
		} else {
			List<Long> jobNotInLocation = new ArrayList<>();
			List<Long> jobAlreadyInLocation = new ArrayList<>();
			List<Location> currentLocationList = jobService.getJobById(jobDTO.getId()).get().getLocationList();

			for (Location loc : currentLocationList) {
				boolean isPresent = false;
				for (String s : locationsIds) {
					if (loc.getId() == Long.valueOf(s))
						isPresent = true;
					if (isPresent)
						jobNotInLocation.add(loc.getId());
					else
						jobAlreadyInLocation.add(loc.getId());
				}
				for (long locationId : jobNotInLocation) {
					Location locationForJobRemoval = locationRepository.getById(locationId);
					List<Job> jobList = locationForJobRemoval.getJobs();
					List<Integer> idsToBeRemoved = new ArrayList<>();
					int x = 0;
					for (Job jb : jobList) {
						if (jb.getId() == jobDTO.getId())
							idsToBeRemoved.add(x);
						x++;
					}

					for (int y : idsToBeRemoved) {
						locationForJobRemoval.getJobs().remove(y);
					}
					locationRepository.save(locationForJobRemoval);
				}

				for (String s : locationsIds) {
					Location location = locationRepository.getById(Long.valueOf(s));
					if (!jobAlreadyInLocation.contains(Long.valueOf(s)))
						location.getJobs().add(job);
					locationRepository.save(location);
				}
			}
		}

		jobService.addJob(job);
		return "redirect:/admin/jobs";

	}

	@GetMapping("/admin/job/delete/{id}")
	public String deleteJob(@PathVariable long id) {

		String type = "job";
		if (jobRespository.findById(id).isPresent()) {

			Job jobToBeDeleted = jobService.getJobById(id).get();
			for (Location loc : jobToBeDeleted.getLocationList()) {
				loc.getJobs().remove(jobToBeDeleted);
				locationService.addLocation(loc);
			}
			jobService.removeJobById(id);
		}

		else {
			//System.out.println("Cannot delete the job");
		}
		return "redirect:/admin/jobs";

	}

	/*
	 * Function to update the jobs
	 */
	@GetMapping("/admin/job/update/{id}")
	public String editJob(@PathVariable Long id, Model model) {
		Job job = jobService.getJobById(id).get();
		JobDTO jobDTO = new JobDTO();

		jobDTO.setId(job.getId());
		jobDTO.setTitle(job.getTitle());
		jobDTO.setShortDescription(job.getShortDescription());
		jobDTO.setDescription(job.getDescription());
		jobDTO.setPostedBy(job.getPostedBy());
		jobDTO.setSkills(job.getSkills());
		jobDTO.setIsJobActive(job.getIsJobActive());
		jobDTO.setExperienceInYears(job.getExperienceInYears());

		model.addAttribute("jobDTO", jobDTO);
		model.addAttribute("locations", locationService.getAllLocations());
		model.addAttribute("locationsSelectedForThisJob", jobService.getJobById(id).get().getLocationList());

		return "jobsAdd";
	}

	@GetMapping("/admin/activateDeactivateJob/{id}/{action}")
	@ResponseBody
	@CrossOrigin("*")
	public String activateDeactivateJob(@PathVariable(name = "id") long id, @PathVariable String action) {
		//System.out.println("Requested for Job action = " + action + " for Job id= " + id);
		Job job = jobService.getJobById(id).get();

		if (action.equals("ActivateJob")) {
			job.setIsJobActive(true);
			jobService.addJob(job);
			return "Job Activated/Published";
		}

		else {
			job.setIsJobActive(false);
			jobService.addJob(job);
			return "Job Deactivate/Unpublished";
		}
	}

	/*
	 * ===========All Function below are related to office location ==============
	 */

	/*
	 * Method to show the locations.html
	 *
	 */

	@GetMapping("/admin/locations")
	public String getLocations(Model model) {
		model.addAttribute("locations", locationService.getAllLocations());
		return "locations";
	}

	/*
	 * Add get and post mapping for showing and doing some processing after
	 * submitting the form
	 */

	@GetMapping("/admin/locations/add")
	public String locationAddGet(Model model) {
		model.addAttribute("locationDTO", new LocationDTO());
		return "locationsAdd";
	}

	@PostMapping("/admin/locations/add")
	public String locationAddPost(@ModelAttribute("locationDTO") LocationDTO locationDTO) {

		Location location = new Location();
		location.setId(locationDTO.getId());
		location.setCity(locationDTO.getCity());

		locationService.addLocation(location);
		return "redirect:/admin/locations";
	}

	/*
	 * Function to delete the location
	 */
	@GetMapping("/admin/location/delete/{id}")
	public String deleteLocation(@PathVariable long id) {
		locationService.removeLocationById(id);
		return "redirect:/admin/locations";
	}

	/*
	 * Function to edit the location
	 */

	@GetMapping("/admin/location/edit/{id}")
	public String editLocation(@PathVariable long id, Model model) {
		Optional<Location> location = locationService.getLocationById(id);
		if (location.isPresent()) {
			model.addAttribute("locationDTO", location.get());
			return "locationsAdd";
		}

		else
			return "404";
	}

	/*
	 * ===========All Function below are related to pages =============
	 */

	/*
	 * Method to show the pages.html
	 *
	 */

	@GetMapping("/admin/pages")
	public String getPages(Model model) {
		model.addAttribute("pages", pageService.getAllPages());
//		String pageLink =
//		HttpServletRequest request = ;

		return "pages";
	}

	/*
	 * Method to add a page but need implement both get and post mapping for adding
	 * the university
	 */

	@GetMapping("/admin/pages/add")
	public String pagesAddGet(Model model) {
		model.addAttribute("pageDTO", new PageDTO());
		return "pagesAdd";
	}

	/*
	 * The post mapping will be able to create a new file at the run time
	 *
	 */

//	@ResponseBody
	@PostMapping(path = "/admin/pages/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String pagesAddPost(@ModelAttribute("pageDTO") PageDTO pageDTO, HttpServletRequest request,
			@RequestParam(value = "pageUpdateCheck", required = false) String pageUpdateCheck,
			@RequestParam(name = "bannerImageFile", required = false) MultipartFile bannerImageFile) {

		/*
		 * Create a new time stamp and initialize the timestamp with null Check if the
		 * entry in database is there for the date of creation... If it is not then
		 * initialise the time stamp with a new date.
		 */

//		Page page = new Page();
		Page page;

		if (bannerImageFile != null && !bannerImageFile.isEmpty()) {
			try {

				Path fileNameAndPath = Paths.get(fileUtils.getResourcePath("resources.assets.newBannerImageAddDir"), bannerImageFile.getOriginalFilename());
				Files.write(fileNameAndPath, bannerImageFile.getBytes());

			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

		if (pageDTO.getId() == null) {
			//System.out.println("New Page is Being created..............");
			page = new Page();
			page.setDateOfCreation(new Date());
		} else {
			//System.out.println("Update Page Operation happening..................");
			page = pageService.getPageById(pageDTO.getId()).get();
			if (page.getBannerImageName() != null)
				pageDTO.setBannerImageName(page.getBannerImageName());
		}

		page.setId(pageDTO.getId());
		page.setPageTitle(pageDTO.getPageTitle().strip());
		page.setCustomUri(pageDTO.getCustomUri().strip());
		page.setEnableHeader(pageDTO.getEnableHeader());
		page.setBannerHeading(pageDTO.getBannerHeading());
		page.setBannerSubHeading(pageDTO.getBannerSubHeading());
		page.setMainSection(pageDTO.getMainSection());
		if (bannerImageFile != null && !bannerImageFile.isEmpty()) {
			page.setBannerImageName(bannerImageFile.getOriginalFilename());
			pageDTO.setBannerImageName(page.getBannerImageName());
		}
//		else if(bannerImageFile == null && pageUpdateCheck != null && pageUpdateCheck.equals("true")) {
//			//System.out.println("Check 2 ----------------------------------------->");
//			page.setBannerImageName(pageService.getPageById(pageDTO.getId()).get().getBannerImageName());
//		}
		page.setBannerImageAlt(pageDTO.getBannerImageAlt());
		page.setCssCode(pageDTO.getCssCode());
		page.setJsCode(pageDTO.getJsCode());
		page.setMetaTitle(pageDTO.getMetaTitle());
		page.setMetaKeyword(pageDTO.getMetaKeyword());
		page.setMetaDescription(pageDTO.getMetaDescription());

		/*
		 * Creating a new html template
		 */
		String extention = ".html";
		/*
		 * Before creating the html file, we have to ensure that two files do not get
		 */
		String preProcessFileName = pageDTO.getPageTitle().toLowerCase().strip();
//		preProcessFileName.toLowerCase();

		preProcessFileName = preProcessFileName.replaceAll("( )+", " ");

		preProcessFileName = preProcessFileName.replaceAll("[^a-zA-Z0-9]", " ");

		preProcessFileName = preProcessFileName.strip();

		//System.out.println("The Pre Process of file name " + preProcessFileName);

		/*
		 * String preProcessFileName = postDTO.getPostTitle().toLowerCase().strip();
		 * 
		 * preProcessFileName = preProcessFileName.replaceAll("( )+", " ");
		 * 
		 * preProcessFileName = preProcessFileName.replaceAll("[^a-zA-Z0-9]", " ");
		 * 
		 * preProcessFileName = preProcessFileName.strip();
		 * 
		 * //System.out.println("The Pre Process of file name " + preProcessFileName);
		 * 
		 * String htmlFileName = preProcessFileName.replaceAll(" ", "-");
		 */

		String htmlFileName = preProcessFileName.replaceAll(" ", "-");
		List<Page> allPages = pageRepository.findAll();
		Iterator<Page> iterator = allPages.iterator();

		/*
		 * Rename the file if the file with the same name already exist
		 */

		int count = 0;
		while (iterator.hasNext()) {
			Page pageUnderEvaluation = iterator.next();
			if (pageUnderEvaluation.getMetaTitle().equalsIgnoreCase(htmlFileName)) {
				htmlFileName += ++count;
			}
		}

		// Problem will occur when user will enter 3 the same name for more than 2
		// times...
		// Some code will have to be written to handle this problem using string and
		// regex manipulation
		// htmlFileName.

		page.setExtractedFileName(htmlFileName);

		htmlFileName += extention;
		String pagesLink = htmlFileName;
		try {
			Path fileNameAndPath = Paths.get(fileUtils.getResourcePath("resources.pages.newPageAddDir"), htmlFileName);
//			Files.createFile(fileNameAndPath);
			Files.write(fileNameAndPath, htmlFileName.getBytes());

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		String hostName = request.getHeader("host");
		//System.out.println(hostName);

		pagesLink = hostName + "/addedPages/";
		String currentPageLink = pagesLink + htmlFileName;

		/*
		 * Write a code to create a page link.
		 */

		//System.out.println(pagesLink);

		/*
		 * Now save the file name in the database so as to access the file in the future
		 * while updating... Searching with the exact file name will be required.
		 */
		pageDTO.setFileName(htmlFileName);
		page.setFileName(pageDTO.getFileName());

		/*
		 * Lets save the link of the file in the database
		 */
		pageDTO.setPageLink(currentPageLink);
		page.setPageLink(pageDTO.getPageLink());
		if ((pageDTO.getEnableHeader() != null && !pageDTO.getEnableHeader().equals("true"))
				|| pageDTO.getEnableHeader() == null) {
			page.setBannerHeading(null);
			pageDTO.setBannerHeading(page.getBannerHeading());
			page.setBannerImageAlt(null);
			pageDTO.setBannerImageAlt(page.getBannerImageAlt());
			page.setBannerImageName(null);
			pageDTO.setBannerImageName(page.getBannerImageName());
			page.setBannerSubHeading(null);
			pageDTO.setBannerSubHeading(page.getBannerSubHeading());
		}
		pageService.addPage(page);
//		htmlPage

		/*
		 * Logic for adding the content in the file to be over here It the end publish
		 * the page...
		 */

		//System.out.println("\n\n\n\n\n\n Main Section preview" + pageDTO.getMainSection());
		String codeInFile;
		if (pageDTO.getEnableHeader() != null && pageDTO.getEnableHeader().equals("true")) {
			codeInFile = htmlBoilerPlate(pageDTO.getMetaTitle(), pageDTO.getMetaKeyword(), pageDTO.getBannerHeading(),
					pageDTO.getBannerSubHeading(), pageDTO.getMetaDescription(), pageDTO.getMainSection(),
					pageDTO.getJsCode(), pageDTO.getCssCode(), pageDTO.getBannerImageName(),
					pageDTO.getBannerImageAlt());
			//System.out.println("The following code will be there in the file " + codeInFile);
			pageDTO.setConsolidatedHTMLCode(codeInFile);
			page.setConsolidatedHTMLCode(pageDTO.getConsolidatedHTMLCode());
		} else {

			codeInFile = htmlBoilerPlateWithoutHeader(pageDTO.getMetaTitle(), pageDTO.getMetaKeyword(),
					pageDTO.getMetaDescription(), pageDTO.getMainSection(), pageDTO.getJsCode(), pageDTO.getCssCode());
			//System.out.println("The following code will be there in the file " + codeInFile);
			pageDTO.setConsolidatedHTMLCode(codeInFile);
			page.setConsolidatedHTMLCode(pageDTO.getConsolidatedHTMLCode());
		}

		try {
			pushCodeInFile(codeInFile, pageDTO.getFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}

		page.setLastModified(pageDTO.getLastModified());
		//System.out.println("page Added sucessfully" + codeInFile);
		pageService.addPage(page);
//		String pageToReturn = "redirect:/viewPages/"+htmlFileName;
		//System.out.println(page.toString());
		return "redirect:/admin/pages";

	}

	private void pushCodeInFile(String codeInFile, String fileName) throws IOException {
		// TODO Auto-generated method stub
		Path fileNameAndPath = Paths.get(fileUtils.getResourcePath("resources.pages.newPageAddDir"), fileName);
		Files.write(fileNameAndPath, codeInFile.getBytes());
	}

	private String htmlBoilerPlate(String metaTitle, String metaKeyword, String bannerHeading, String bannerSubheading,
			String metaDescription, String mainSection, String jsCode, String cSSCode, String bannerImageFileName,
			String bannerAltText) {
		// TODO Auto-generated method stub
		/*
		 * initial code
		 */

		String boilerPlate = "<!DOCTYPE html>\r\n"
				+ "<html lang=\"en\" xmlns:layout=\"http://www.ultraq.net.nz/thymeleaf/layout\"\r\n"
				+ "	layout:decorate=\"_LivePagelayout\">\r\n" + "<head>\r\n" + "<title>" + metaTitle + "</title>\r\n"
				+ "  <meta name=\"description\"  content=\" " + metaDescription + "  \" />" + "\r\n"
				+ "  <meta name=\"keywords\"  content=\" " + metaKeyword + "  \" />" + "\r\n"
				+ "<script type=\"text/javascript\" src=\"/viewPagesAssets/js/customGlobalHeader/globalHeader.js\"></script>"
				+ "<style>\r\n" + cSSCode + "</style>\r\n" + "<body id=\"page-top\">\r\n" + "\r\n"
				+ "	<!-- Content Wrapper -->\r\n" + "	<div layout:fragment=\"contentPlus\">\r\n"
				+ "  <section class=\"top_avanse_banner_area abroad_top_bg\">\r\n" + "            \r\n"
				+ "            <div class=\"container\">\r\n"
				+ "                <div class=\"row align-items-center\">\r\n"
				+ "                    <div class=\"col-lg-7\">\r\n"
				+ "                        <div class=\"h_avaneses_content\">\r\n"
				+ "                            <h2 class=\"wow fadeInLeft\" data-wow-delay=\"0.4s\"> " + bannerHeading
				+ "</h2>\r\n" + "                            <h3 class=\"wow fadeInLeft\" data-wow-delay=\"0.6s\"> "
				+ bannerSubheading + "</h3>\r\n" + "                        </div>\r\n"
				+ "                    </div>\r\n" + "                    <div class=\"col-lg-5\">\r\n"
				+ "                        <div class=\"h_avanse_img\">\r\n"
				+ "                            <img src=\"/viewPagesAssets/img/userAddedBannerImages/"
				+ bannerImageFileName + "\" alt=\"" + bannerAltText + "\">\r\n" + "                        </div>\r\n"
				+ "                    </div>\r\n" + "                </div>\r\n" + "            </div>\r\n"
				+ "        </section>"

				+ mainSection + "\r\n"
				+ " 	</body> " + "\r\n"
				+ " 	</html> " + "\r\n"

				+ "    <!-- Optional JavaScript -->\r\n" + jsCode + "\r\n" ;

		return boilerPlate;
	}

	private String htmlBoilerPlateWithoutHeader(String metaTitle, String metaKeyword, String metaDescription,
			String mainSection, String jsCode, String cSSCode) {
		// TODO Auto-generated method stub
		/*
		 * initial code
		 */

		String boilerPlate = "<!DOCTYPE html>\r\n"
				+ "<html lang=\"en\" xmlns:layout=\"http://www.ultraq.net.nz/thymeleaf/layout\"\r\n"
				+ "	layout:decorate=\"_LivePagelayout\">\r\n" + "<head>\r\n" + "<title>" + metaTitle + "</title>\r\n"
				+ "  <meta name=\"description\"  content=\" " + metaDescription + "  \" />" + "\r\n"
				+ "  <meta name=\"keywords\"  content=\" " + metaKeyword + "  \" />" + "\r\n"
				+ "<script type=\"text/javascript\" src=\"/viewPagesAssets/js/customGlobalHeader/globalHeader.js\"></script>"
				+ "<style>\r\n" + cSSCode + "</style>\r\n" + "<body id=\"page-top\">\r\n" + "\r\n"
				+ "	<!-- Content Wrapper -->\r\n" + "	<div layout:fragment=\"contentPlus\">\r\n" + mainSection
				+ "\r\n" + "    <!-- Optional JavaScript -->\r\n" + jsCode + "\r\n" + " 	</body> " + "\r\n";

		return boilerPlate;
	}

	/*
	 * Function to delete a page from the database and the server
	 * 
	 */

	@GetMapping("/admin/page/delete/{id}")
	public String deletePage(@PathVariable long id) {
		/*
		 * Basic check if the page already exist or not
		 */
		String type = "page";
		if (pageRepository.findById(id).isPresent()) {
			deleteHtmlFileFromServer(id, type);
			pageService.removePageById(id);
		} else {
			//System.out.println("Cannot delete the page");
		}
		return "redirect:/admin/pages";
	}

	private void deleteHtmlFileFromServer(@PathVariable long id, String type) {
		// TODO Auto-generated method stub
		if (type.equalsIgnoreCase("page")) {

			Page htmlFileToBeDeleted = pageService.getPageById(id).get();

			String theFile = htmlFileToBeDeleted.getFileName();

			File file = new File(fileUtils.getResourcePath("resources.pages.newPageAddDir") + File.separator + theFile);

			if (file.exists())
				file.delete();
		}

		else if (type.equalsIgnoreCase("post")) {
			Post htmlFileToBeDeleted = postService.getPostById(id).get();
			String theFile = htmlFileToBeDeleted.getFileName();

			File file = new File(fileUtils.getResourcePath("resources.pages.newPostAddDir") + File.separator + theFile);

			if (file.exists())
				file.delete();

		}
	}

	/*
	 * 
	 * 
	 * Write a function to write the code into the html file This function will be
	 * called in both add and edit function... We Dont need to call the write to
	 * file using edit becuase edit will eventually be called from only pages add...
	 * But keeping it in a function is a better idea, any day...
	 * 
	 * 
	 */

	@GetMapping("/admin/page/edit/{id}")
	public String editPage(@PathVariable long id, Model model) {
		Page page = pageService.getPageById(id).get();
		PageDTO pageDTO = new PageDTO();
		pageDTO.setId(page.getId());
		pageDTO.setPageTitle(page.getPageTitle());
		pageDTO.setCustomUri(page.getCustomUri());

		pageDTO.setBannerHeading(page.getBannerHeading());
		pageDTO.setBannerSubHeading(page.getBannerSubHeading());
		pageDTO.setPageLink(page.getPageLink());
		pageDTO.setMainSection(page.getMainSection());
		pageDTO.setBannerImageName(page.getBannerImageName());
		pageDTO.setBannerImageAlt(page.getBannerImageAlt());
		pageDTO.setCssCode(page.getCssCode());
		pageDTO.setFileName(page.getFileName());
		pageDTO.setJsCode(page.getJsCode());
		pageDTO.setMetaTitle(page.getMetaTitle());
		pageDTO.setMetaKeyword(page.getMetaKeyword());
		pageDTO.setMetaDescription(page.getMetaDescription());
		pageDTO.setEnableHeader(page.getEnableHeader());
		model.addAttribute("pageDTO", pageDTO);
		model.addAttribute("isPageUpdate", "true");
		return "pagesAdd";
	}

	@GetMapping("/admin/activateDeactivatePage/{id}/{action}")
	@ResponseBody
	@CrossOrigin("*")
	public String activateDeactivatePage(@PathVariable(name = "id") long id, @PathVariable String action) {
		//System.out.println("Requested for Page action = " + action + " for Page id= " + id);
		Page page = pageService.getPageById(id).get();

		if (action.equals("ActivatePage")) {
			page.setIsPageActive(true);
			pageService.addPage(page);
			return "Page Activated/Published";

		}

		else {
			page.setIsPageActive(false);
			pageService.addPage(page);
			return "Page Deactivate/Unpublished";
		}
	}

	/*
	 * Below Function is to access the global CSS file located at
	 * /viewPagesAssets/css/style.css"
	 */

	@GetMapping("/admin/globalHeader")
	public String globalHeaderAddGet(Model model) {
		model.addAttribute("header", new Header());
		return "globalHeader";
	}

	@PostMapping("/admin/globalHeader")
	public String globalHeaderAddPost(@RequestParam("globalHeaderCode") String globalHeaderCode) {

		File globalHeaderFile = new File(fileUtils.getResourcePath("resources.assets.globalHeaderFilePath"));
		//System.out.println("InputCode----->" + globalHeaderCode);

		try {
			FileWriter fwr = new FileWriter(globalHeaderFile);
			fwr.write(globalHeaderCode);
			fwr.close();
		} catch (IOException e) {
			//System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return "redirect:/admin/globalHeader";
	}

	/*
	 * Below functions will be used to create the posts
	 */

	@GetMapping("/admin/posts")
	public String getPosts(Model model) {
		model.addAttribute("posts", postService.getAllPosts());
		return "posts";
	}

	@GetMapping("/admin/posts/add")
	public String postsGet(Model model) {
		model.addAttribute("postDTO", new PostDTO());
		model.addAttribute("postCategories", postCategoryService.getAllPostCategories());
		return "postsAdd";
	}

	@PostMapping(path = "/admin/posts/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String blogPostsAddPostMap(@ModelAttribute("postDTO") PostDTO postDTO, HttpServletRequest request,
			@RequestParam(name = "featuredImageFile", required = false) MultipartFile featuredImageFile,
			@RequestParam("selectedCategories") String[] categoriesIds,
			@RequestParam(value = "updateOperation", required = false) String isUpdating) {

		/*
		 * Create a new time stamp and initialize the timestamp with null Check if the
		 * entry in database is there for the date of creation... If it is not then
		 * initialise the time stamp with a new date.
		 */

		Post post;

		if (featuredImageFile != null && !featuredImageFile.isEmpty()) {
			try {

				Path fileNameAndPath = Paths.get(fileUtils.getResourcePath("resources.assets.newFeaturedImageAddDir"), featuredImageFile.getOriginalFilename());
				Files.write(fileNameAndPath, featuredImageFile.getBytes());

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

		if (postDTO.getId() == null) {
			post = new Post();
			post.setDateOfCreation(new Date());
		}

		else {
			post = postService.getPostById(postDTO.getId()).get();
			if (post.getFeaturedImageName() != null)
				postDTO.setFeaturedImageName(post.getFeaturedImageName());
		}

		Date date = new Date();
		String tempDate = new SimpleDateFormat("dd MMMM, yyyy").format(date);
//		//System.out.println("TESTING ------------> " + isUpdating);
		post.setId(postDTO.getId());
		post.setPostTitle(postDTO.getPostTitle().strip());
		post.setHeading(postDTO.getHeading());
		post.setSubHeading(postDTO.getSubHeading());
		post.setMainSection(postDTO.getMainSection());
		if (featuredImageFile != null && !featuredImageFile.isEmpty()) {
			post.setFeaturedImageName(featuredImageFile.getOriginalFilename());
			postDTO.setFeaturedImageName(post.getFeaturedImageName());
		}
//		else if (isUpdating != null) {
//			//System.out.println(
//					"Image test edit ---> " + postService.getPostById(postDTO.getId()).get().getFeaturedImageName()
//							+ "PostDTO id = " + postDTO.getId());
//			post.setFeaturedImageName(postService.getPostById(postDTO.getId()).get().getFeaturedImageName());
//		}
//		post.setFeaturedImageName(postDTO.getFeaturedImageName());
		post.setFeaturedImageAltText(postDTO.getFeaturedImageAltText());
		post.setMetaTitle(postDTO.getMetaTitle());
		post.setMetaDescription(postDTO.getMetaDescription());
		post.setDateOfPostCreation(tempDate);

		/*
		 * Creating a new html template
		 */
		String extention = ".html";
		/*
		 * Before creating the html file, we have to ensure that two files do not get
		 */

		String preProcessFileName = postDTO.getPostTitle().toLowerCase().strip();

		preProcessFileName = preProcessFileName.replaceAll("( )+", " ");

		preProcessFileName = preProcessFileName.replaceAll("[^a-zA-Z0-9]", " ");

		preProcessFileName = preProcessFileName.strip();

		//System.out.println("The Pre Process of file name " + preProcessFileName);

		String htmlFileName = preProcessFileName.replaceAll(" ", "-");

		List<Post> allPosts = postRepository.findAll();
		Iterator<Post> iterator = allPosts.iterator();

		/*
		 * Rename the file if the file with the same name already exist
		 */

		int count = 0;
		while (iterator.hasNext()) {
			Post postUnderEvaluation = iterator.next();
			if (postUnderEvaluation.getMetaTitle().equalsIgnoreCase(htmlFileName)) {
				htmlFileName += ++count;
			}
		}

		// Problem will occur when user will enter 3 the same name for more than 2
		// times...
		// Some code will have to be written to handle this problem using string and
		// regex manipulation
		// htmlFileName.

		post.setExtractedFileName(htmlFileName);

		htmlFileName += extention;

		try {
			Path fileNameAndPath = Paths.get(fileUtils.getResourcePath("resources.pages.newPostAddDir"), htmlFileName);

			Files.createFile(fileNameAndPath);

		} catch (IOException e) {
			// TODO: handle exception
		}

		postService.addPost(post);

		if (isUpdating == null) {
			for (String s : categoriesIds) {
				PostCategory postCategory = postCategoryRepository.getById(Long.valueOf(s));
				postCategory.getPostList().add(post);
				postCategoryRepository.save(postCategory);
			}
		} else {
			List<Long> postNotInCategory = new ArrayList<>();
			List<Long> postAlreadyInCategory = new ArrayList<>();
			List<PostCategory> currentCategoryList = postService.getPostById(postDTO.getId()).get()
					.getPostCategoryList();
			for (PostCategory pc : currentCategoryList) {
				boolean isPresent = false;
				for (String s : categoriesIds) {
					if (pc.getId() == Long.valueOf(s))
						isPresent = true;
				}
				if (!isPresent)
					postNotInCategory.add(pc.getId());
				else
					postAlreadyInCategory.add(pc.getId());

			}
			for (long categoryId : postNotInCategory) {
				PostCategory postCategoryForPostRemoval = postCategoryRepository.getById(categoryId);
				List<Post> postList = postCategoryForPostRemoval.getPostList();
				List<Integer> idsToBeRemoved = new ArrayList<>();
				int x = 0;
				for (Post po : postList) {
					if (po.getId() == postDTO.getId())
						idsToBeRemoved.add(x);
					x++;
				}
				for (int y : idsToBeRemoved) {
					postCategoryForPostRemoval.getPostList().remove(y);
				}
				postCategoryRepository.save(postCategoryForPostRemoval);
			}
			for (String s : categoriesIds) {
				PostCategory postCategory = postCategoryRepository.getById(Long.valueOf(s));
				// if(!postCategory.getPostList().contains(postService.getPostById(postDTO.getId())))
				if (!postAlreadyInCategory.contains(Long.valueOf(s)))
					postCategory.getPostList().add(post);
				postCategoryRepository.save(postCategory);

			}
		}

//////		String hostName = request.getHeader("host");
////		//System.out.println(hostName);
////		
////		String postLink = hostName + "/addedPages/";
//		String currentPageLink = postLink + htmlFileName;

		/*
		 * Write a code to create a page link.
		 */
//		//System.out.println(postLink);

		/*
		 * Now save the file name in the database so as to access the file in the future
		 * while updating... Searching with the exact file name will be required.
		 */
		postDTO.setFileName(htmlFileName);
		post.setFileName(postDTO.getFileName());

		/*
		 * Get the date in
		 */

		/*
		 * Lets save the link of the file in the database
		 */
//		postDTO.setPostLink(currentPageLink);
		postDTO.setPostLink(postDTO.getPostLink());
		postService.addPost(post);
//		htmlPage

		/*
		 * Logic for adding the content in the file to be over here It the end publish
		 * the page...
		 */

		//System.out.println("\n\n\n\n\n\n Main Section preview" + postDTO.getMainSection());

		String codeInFile = htmlBlogLayout(postDTO.getMetaTitle(), postDTO.getHeading(), postDTO.getSubHeading(),
				postDTO.getMetaDescription(), postDTO.getMainSection(), postDTO.getFeaturedImageName());
		//System.out.println("The following code will be there in the blog file " + codeInFile);
		postDTO.setConsolidatedHTMLCode(codeInFile);
		post.setConsolidatedHTMLCode(postDTO.getConsolidatedHTMLCode());

		try {
			pushCodeInBlogFile(codeInFile, postDTO.getFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}

//		post.setLastModified(postDTO.getLastModified());
		//System.out.println("page Added sucessfully" + codeInFile);
		postService.addPost(post);
//		String pageToReturn = "redirect:/viewPages/"+htmlFileName;
		//System.out.println(post.toString());
		return "redirect:/admin/posts";

	}

	private void pushCodeInBlogFile(String codeInFile, String fileName) throws IOException {
		// TODO Auto-generated method stub
		Path fileNameAndPath = Paths.get(fileUtils.getResourcePath("resources.pages.newPostAddDir"), fileName);
		Files.write(fileNameAndPath, codeInFile.getBytes());
	}

	private String htmlBlogLayout(String metaTitle, String heading, String subheading, String metaDescription,
			String mainSection, String featuredImageFileName) {

		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int day = localDate.getDayOfMonth();

		Calendar cal = Calendar.getInstance();
		String month = new SimpleDateFormat("MMMM").format(cal.getTime());
		String dateOfBlogPost = new SimpleDateFormat("dd MMMM, YYYY").format(date);

		String layoutCode = "<!DOCTYPE html>\r\n"
				+ "<html lang=\"en\" xmlns:layout=\"http://www.ultraq.net.nz/thymeleaf/layout\"\r\n"
				+ "	layout:decorate=\"_LivePagelayout\">\r\n" + "<head>\r\n"
				+ "<!-- KEYWORDTOFINDGLOBALHEADERINSERTIONCODESPACESTART -->\r\n" + "\r\n"
				+ "<!-- KEYWORDTOFINDGLOBALHEADERINSERTIONCODESPACEEND -->"
//				+ header to be implemented later
				+ "<title>" + metaTitle + "</title>\r\n" + "  <meta name=\"description\"  content=\" " + metaDescription
				+ "  \" />" + "\r\n"

				+ "<script type=\"text/javascript\" src=\"/viewPagesAssets/js/customGlobalHeader/globalHeader.js\"></script>"
				+ "\r\n"

				+ "</head>\r\n" + "<body id=\"page-top\">\r\n" + "\r\n" + "	<!-- Content Wrapper -->\r\n"
				+ "	<div layout:fragment=\"contentPlus\">"
				+ "<section class=\" pt-3 pb-3\" style=\"background: #02afb3\">\r\n" + "           \r\n"
				+ "            <div class=\"container\">\r\n"
				+ "                <div class=\"breadcrumb_content text-center\">\r\n"
				+ "                    <h1 class=\"f_p f_500 f_size_40 w_color l_height50 mb_20\">" + heading
				+ "</h1>\r\n" + "                    <p class=\"f_400 w_color f_size_21 l_height28\">" + subheading
				+ "</p>\r\n" + "                </div>\r\n" + "            </div>\r\n" + "        </section>\r\n"
				+ "      \r\n" + "\r\n" + "        <section class=\"blog_area sec_pad\">\r\n"
				+ "            <div class=\"container\">\r\n" + "                <div class=\"row\">\r\n"
				+ "                    <div class=\"col-lg-8 blog_sidebar_left\">\r\n"
				+ "                        <div class=\" mb_50\">\r\n"
				+ "                            <img id=\"myFeaturedImage\" class=\"img-fluid\" src=\"/viewPagesAssets/img/userAddedFeaturedImages/"
				+ featuredImageFileName + "\" alt=\"\">\r\n"
				+ "                            <div class=\"blog_content\">\r\n"
				+ "                                <div class=\"post_date\">\r\n"
				+ "                                    <h2>" + day + " <span>" + month + "</span></h2>\r\n"
				+ "                                </div>\r\n"
				+ "                                <!-- <div class=\"entry_post_info\">\r\n"
				+ "                                    By: <a href=\"#\">Admin</a>\r\n"
				+ "                                    <a href=\"#\">2 Comments</a>\r\n"
				+ "                                    <a href=\"#\">SaasLand</a>\r\n"
				+ "                                </div> -->\r\n"
				+ "                                <!-- <a href=\"#\">\r\n"
				+ "                                    <h5 class=\"f_p f_size_20 f_500 t_color mb-30\">Lorem Ipsum is simply dummy text of the printing and typesetting</h5>\r\n"
				+ "                                </a> -->\r\n"
				+ "                                <p class=\"f_400 mb-30\">" + mainSection + "</p>"

				+ "                                <!--<blockquote class=\"blockquote mb_40\">\r\n"
				+ "                                    <h6 class=\"mb-0 f_size_18 l_height30 f_p f_400\">Elizabeth ummm I'm telling bodge\r\n"
				+ "                                        spend a penny say wellies say James Bond, bubble and squeak a such a fibber you\r\n"
				+ "                                        mug quaint cack what.!</h6>\r\n"
				+ "                                </blockquote>-->\r\n"

				+ "                                <div class=\"post_share\">\r\n"
				+ "                                    <div class=\"post-nam\"> Share: </div>\r\n"
				+ "                                    <div class=\"flex\">\r\n"
				+ "                                        <a href=\"https://www.facebook.com/AvanseEducationLoan\"><i class=\"ti-facebook\"></i>Facebook</a>\r\n"
				+ "                                        <a href=\"https://twitter.com/AvanseEduLoan\"><i class=\"ti-twitter\"></i>Twitter</a>\r\n"
				+ "                                    </div>\r\n" + "                                </div>\r\n"
				+ "                                <!-- <div class=\"post_tag d-flex\">\r\n"
				+ "                                    <div class=\"post-nam\"> Tags: </div>\r\n"
				+ "                                    <a href=\"#\">Wheels</a>\r\n"
				+ "                                    <a href=\"#\">Saasland</a>\r\n"
				+ "                                    <a href=\"#\">UX/Design</a>\r\n"
				+ "                                </div>\r\n"
				+ "                                <div class=\"media post_author mt_60\">\r\n"
				+ "                                    <img class=\"rounded-circle\" src=\"img/blog-grid/author_img.png\" alt=\"\">\r\n"
				+ "                                    <div class=\"media-body\">\r\n"
				+ "                                        <h5 class=\"f_p t_color3 f_size_18 f_500\">Bodrum Salvador</h5>\r\n"
				+ "                                        <h6 class=\"f_p f_size_15 f_400 mb_20\">Editor</h6>\r\n"
				+ "                                        <p>Tinkety tonk old fruit Harry gormless morish Jeffrey what a load of rubbish\r\n"
				+ "                                            burke what a plonker hunky-dory cor blimey guvnor.!</p>\r\n"
				+ "                                    </div>\r\n" + "                                </div> -->\r\n"
				+ "                            </div>\r\n" + "                        </div>\r\n"
				+ "                        <div class=\"blog_post\">\r\n"
				+ "                            <div class=\"widget_title\">\r\n"
				+ "					<h3 class=\"f_p f_size_20 t_color3\">Related Post</h3>\r\n"
				+ "                                <div class=\"border_bottom\"></div>\r\n"
				+ "                            </div>\r\n" + "                            <div class=\"row\">\r\n"

				+ "                                <div th:each=\"relatedPost, iStat: ${relatedThreePosts} \"  class=\"col-lg-4 col-sm-6\">\r\n"

				+ "                                   <a th:href=\"'/blog/'+${relatedPost.extractedFileName}\">\r\n"
				+ "                                    <div class=\"blog_post_item\">\r\n"
				+ "                                        <div class=\"blog_img\">\r\n"
				+ "                                            <img th:src=\"'/viewPagesAssets/img/userAddedFeaturedImages/'+${relatedPost.featuredImageName}\" alt=\"\">\r\n"
				+ "                                        </div>\r\n"
				+ "                                        <div class=\"blog_content\">\r\n"
				+ "                                            <div class=\"entry_post_info\">\r\n"
				+ "                                                <h5 th:text=\"${relatedPost.dateOfPostCreation}\">March 14, 2020</h5>\r\n"
				+ "                                            </div>\r\n"
				+ "                                                <h5 th:text=\"${relatedPost.heading}\" class=\"f_p f_size_16 f_500 t_color\">Why I say old chap that.</h5>\r\n"
				+ "                                            <p th:text=\"${relatedPost.subHeading}\" class=\"f_400 mb-0\">Harry bits and bleeding crikey argy-bargy...</p>\r\n"
				+ "                                        </div>\r\n"
				+ "                                    </div>\r\n" + "                                   </a>\r\n"

				+ "                                </div>\r\n"

				+ "                            </div>\r\n" + "                        </div>\r\n"
				+ "                        \r\n" + "                    </div>\r\n"
				+ "                    <div class=\"col-lg-4\">\r\n"
				+ "                        <div class=\"blog-sidebar\">\r\n"

				+ "<div onmouseleave=\"hideResultBox()\" onmouseenter=\"showResultBox()\" class=\"widget sidebar_widget widget_search\">\r\n"
				+ "	                                <form action=\"/blog\" method=\"get\" class=\"search-form input-group\">\r\n"
				+ "	                                    <input name=\"searchKey\" type=\"search\" class=\"form-control widget_input\" placeholder=\"Search\">\r\n"
				+ "	                                    <button type=\"submit\"><i class=\"ti-search\"></i></button>\r\n"
				+ "	                                </form>\r\n" + "	                         </div>\r\n"
				+ "	                         <div hidden onmouseenter=\"showResultBox()\" onmouseleave=\"hideResultBox()\" id=\"resultBox\" class=\"widget sidebar_widget widget_search\" \r\n"
				+ "	                         	style=\"position: absolute;z-index: 999;\r\n"
				+ "	                         	background-color: white;height: 60vh; \r\n"
				+ "	                         	width: 17.24vw;overflow: auto;box-shadow: 0px 0px 6px 1px gray;\">\r\n"
				+ "	                         	\r\n"
				+ "	                         	<div class=\"container border\">\r\n"
				+ "	                         		<div style=\"cursor: pointer;\">\r\n"
				+ "	                         		<div class=\"row\">\r\n"
				+ "	                         			<div class=\"col-sm-3\">\r\n"
				+ "				                         	<div id=\"imageContainer\">\r\n"
				+ "				                         		<img style=\"height: 80px;width: 80px;\" src=\"http://localhost:8080/viewPagesAssets/img/userAddedFeaturedImages/associate-with-us-top-banner.png\">\r\n"
				+ "		                         			</div>\r\n"
				+ "	                         			</div>\r\n"
				+ "	                         			<div class=\"col-sm-9\">\r\n"
				+ "		                         				<h1 id=\"headingContainer\" style=\"font-size:25px; text-decoration-line: underline;\">sadsd ddf</h1>\r\n"
				+ "		                         			\r\n"
				+ "		                         				<h1 id=\"subHeadingContainer\" style=\"font-size:15px; text-decoration-line: underline;\">asdfsdf</h1>\r\n"
				+ "		                         			\r\n" + "	                         			</div>\r\n"
				+ "	                         		</div>\r\n" + "	                         		</div>\r\n"
				+ "	                         	</div>\r\n" + "	                         \r\n"
				+ "	                         \r\n" + "	                         </div>\r\n"
				+ "	                         <script type=\"text/javascript\">\r\n"
				+ "	                        function showResultBox() {\r\n"
				+ "	                        	document.getElementById('resultBox').hidden=true;\r\n"
				+ "	                        }\r\n" + "	                        function hideResultBox() {\r\n"
				+ "	                        	document.getElementById('resultBox').hidden=true;\r\n"
				+ "	                        }\r\n" + "	                        \r\n"
				+ "	                         \r\n" + "	                         \r\n"
				+ "	                         </script>"

				+ "                            <div class=\"widget sidebar_widget widget_categorie mt_60\">\r\n"
				+ "                                <div class=\"widget_title\">\r\n"
				+ "                                    <h3 class=\"f_p f_size_20 t_color3\">Categories</h3>\r\n"
				+ "                                    <div class=\"border_bottom\"></div>\r\n"
				+ "                                </div>\r\n"
				+ "									<ul th:each =\"postCategory, iStat : ${postCategories}\" class=\"list-unstyled\">\r\n"
				+ "                                    <li> <a href=\"#\"><span th:href=\"${postCategory.name}\"th:text=\"${postCategory.name}\">Education</span></a> </li>\r\n"
				+ "                                    \r\n" + "                                </ul>"
				+ "                            </div>\r\n"
				+ "                            <div class=\"widget sidebar_widget widget_recent_post mt_60\">\r\n"
				+ "                                <div class=\"widget_title\">\r\n"
				+ "                                    <h3 class=\"f_p f_size_20 t_color3\">Recent posts</h3>\r\n"
				+ "                                    <div class=\"border_bottom\"></div>\r\n"
				+ "                                </div>\r\n"
				+ "								<div class=\"media post_item\" th:each = \"post , iStat : ${posts}\">\r\n"
				+ "                                    <!-- <img src=\"/viewPagesAssets/img/blog-grid/post_1.jpg\" alt=\"\"> -->\r\n"
				+ "                                    <img width = \"90px\" height = \"80\" th:src=\"@{/viewPagesAssets/img/userAddedFeaturedImages/{featuredImageName}(featuredImageName=${post.featuredImageName})}\" alt=\"\">\r\n"
				+ "                                    \r\n"
				+ "                                    <div class=\"media-body\">\r\n"
				+ "                                        <a href=\"#\" th:href=\"@{/blog/{extractedFileName}(extractedFileName=${post.extractedFileName})}\" >\r\n"
				+ "                                            <h3 class=\"f_size_16 f_p f_400\"  th:text=\"${post.postTitle}\">Pro</h3>\r\n"
				+ "                                        </a>\r\n"
				+ "                                        <div class=\"entry_post_info\">\r\n"
				+ "                                           \r\n"
				+ "                                            <a th:href=\"@{/blog/{extractedFileName}(extractedFileName=${post.extractedFileName})}\" th:text=\"${post.dateOfPostCreation}\">March 14, 2020</a>\r\n"
				+ "                                        </div>\r\n"
				+ "                                    </div>\r\n" + "                                </div>"
				+ "                            </div>\r\n" + "                            \r\n"
				+ "                            \r\n" + "                        </div>\r\n"
				+ "                    </div>\r\n" + "\r\n" + "                </div>\r\n" + "            </div>\r\n"
				+ "        </section>";

		return layoutCode;
	}

	/*
	 * Function to delete a post from the database and the server
	 *
	 */
	@GetMapping("/admin/post/delete/{id}")
	public String deletePost(@PathVariable long id) {

		String type = "post";
		if (postRepository.findById(id).isPresent()) {
			deleteHtmlFileFromServer(id, type);
			Post postToBeDeleted = postService.getPostById(id).get();
			for (PostCategory pc : postToBeDeleted.getPostCategoryList()) {
				pc.getPostList().remove(postToBeDeleted);
				postCategoryService.addPostCategory(pc);
			}
			postService.removePostById(id);
		}

		else {
			//System.out.println("Cannot delete the page");
		}
		return "redirect:/admin/posts";

	}

	@GetMapping("/admin/post/edit/{id}")
	public String editPost(@PathVariable long id, Model model) {

		Post post = postService.getPostById(id).get();
		PostDTO postDTO = new PostDTO();

		postDTO.setId(post.getId());
		postDTO.setFileName(post.getFileName());
		postDTO.setPostTitle(post.getPostTitle());
		postDTO.setHeading(post.getHeading());
		postDTO.setSubHeading(post.getSubHeading());
		postDTO.setFeaturedImageName(post.getFeaturedImageName());
		postDTO.setFeaturedImageAltText(post.getFeaturedImageAltText());
		postDTO.setMainSection(post.getMainSection());
		postDTO.setConsolidatedHTMLCode(post.getConsolidatedHTMLCode());
		postDTO.setMetaTitle(post.getMetaTitle());
//		postDTO.setMetaDescription(post.getMetaTitle());
		postDTO.setMetaDescription(post.getMetaDescription());

		model.addAttribute("isPostUpdate", "true");

		model.addAttribute("postDTO", postDTO);
		model.addAttribute("postCategories", postCategoryService.getAllPostCategories());
		model.addAttribute("postCategoriesSelectedForThisPost",
				postService.getPostById(id).get().getPostCategoryList());

		return "postsAdd";

	}

	// Activate Deactivate post
	@GetMapping("/admin/activateDeactivatePost/{id}/{action}")
	@ResponseBody
	@CrossOrigin("*")
	public String activateDeactivatePost(@PathVariable(name = "id") long id, @PathVariable String action) {
		//System.out.println("Requested for Post action = " + action + " for Post id= " + id);
		if (action.equals("ActivatePost")) {
			Post post = postService.getPostById(id).get();
			post.setIsPostActive(true);
			postService.addPost(post);
			return "Post Activated!!";
		} else {
			Post post = postService.getPostById(id).get();
			post.setIsPostActive(false);
			postService.addPost(post);
			return "Post De-Activated!!";
		}
	}

	/*
	 * POST CATEGORIES
	 * 
	 * Below functions will be used to create the post categories
	 */

	@GetMapping("/admin/postCategories")
	public String getCategories(Model model) {
		model.addAttribute("postCategories", postCategoryService.getAllPostCategories());
		return "postCategories";

	}

	@GetMapping("/admin/postCategories/add")
	public String getCatAdd(Model model) {

		model.addAttribute("postCategory", new PostCategory());
		return "categoriesAdd";

	}

	@PostMapping("/admin/postCategories/add")
	public String postCatAdd(@ModelAttribute("postCategory") PostCategory postCategory) {

		postCategoryService.addPostCategory(postCategory);
		return "redirect:/admin/postCategories";
	}

//	Delete Method for deleting the categories by id
	@GetMapping("/admin/postCategory/delete/{id}")
	public String deleteCat(@PathVariable long id) {
		postCategoryService.removePostCategoryById(id);
		// categoryService.removeCategoryById(id);
		return "redirect:/admin/postCategories";
	}

//	Update Method for updating the categories
	@GetMapping("/admin/postCategory/edit/{id}")
	public String updateCat(@PathVariable Long id, Model model) {
//		Optional<Category> category = categoryService.getCategoryById(id);
		Optional<PostCategory> postCategory = postCategoryService.getPostCategoryById(id);
		if (postCategory.isPresent()) {
			model.addAttribute("postCategory", postCategory.get());
			return "categoriesAdd";
		} else
			return "404";
	}

	/*
	 * Image upload location for summernote
	 */
	public String sendImageUploadLocation() {
		return "To do";
	}

	/*
	 * -------Functions below are for handelling testimonials
	 */

	@GetMapping("/admin/testimonials")
	public String getTestimonials(Model model) {
//		model.addAttribute("testimonials", testimonialService.getAllTestimonials());
		return listTestimonialsByPage(1, model);
	}

	@GetMapping("/admin/testimonials/page/{pageNum}")
	public String listTestimonialsByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
		org.springframework.data.domain.Page<Testimonial> page = testimonialService.listTestimonialsByPage(pageNum);

		List<Testimonial> testimonials = page.getContent();

		//System.out.println("PageNum =" + pageNum);
		//System.out.println("Total elements= " + page.getNumberOfElements());
		//System.out.println("Total Pages= " + page.getTotalPages());

		long startCount = (pageNum - 1) * testimonialService.TESTIMONIALS_PER_PAGE + 1;
		long endCount = startCount + testimonialService.TESTIMONIALS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("testimonials", testimonials);
		return "testimonials";

	}

	/*
	 * Method to add a university Need both get and post mapping for adding the
	 * university because the request could be of any type...
	 */
	@GetMapping("/admin/testimonials/add")
	public String testimonialsAddGet(Model model) {
		model.addAttribute("testimonialDTO", new TestimonialDTO());
//		model.addAttribute("university", new University());
		return "testimonialsAdd";
	}

	/*
	 * On writing the post mapping you will be able to upload images to the server.
	 */
	@PostMapping("/admin/testimonials/add")
	public String testimonialsAddPost(@ModelAttribute("testimonialDTO") TestimonialDTO testimonialDTO,
			@RequestParam("testimonialImage") MultipartFile file, @RequestParam("imgName") String imgName)
			throws IOException {

		Testimonial testimonial = new Testimonial();

		Date date = new Date();

		/*
		 * Get the milliseconds using the date object since 1970
		 */

		long millisecsFrom1970 = date.getTime();

		/*
		 * Convert the millisecs to String that can be pushed into the database
		 */

		String modifiedFileNameByDate = String.valueOf(millisecsFrom1970);

		testimonial.setId(testimonialDTO.getId());
		testimonial.setName(testimonialDTO.getName());
		testimonial.setInfo(testimonialDTO.getInfo());
		testimonial.setMessage(testimonialDTO.getMessage());
		testimonial.setPicFileName(testimonialDTO.getPicFileName());

		/*
		 * Create the imageUUID and using the nio package get the filename and the path
		 */
		String imageUUID;
		if (!file.isEmpty()) {

			/*
			 * Extract The file extention using get Extention method and store in the ext
			 * variable.
			 */
			String ext = FilenameUtils.getExtension(file.getOriginalFilename());

			/*
			 * Generate the complete file name with extention
			 */
			imageUUID = modifiedFileNameByDate + '.' + ext;

			/*
			 * Use the nio library to do the stream operations. Paas the universal Unique ID
			 * and the university upload Directory
			 */
			Path fileNameAndPath = Paths.get(fileUtils.getResourcePath("resources.images.testimonialPersonUploadDir"), imageUUID);
			Files.write(fileNameAndPath, file.getBytes());

		} else {
			imageUUID = imgName;

		}

		/*
		 * Pass the UUID into the imagename of the university...
		 */
		testimonial.setPicFileName(imageUUID);

		/*
		 * Use the university Service to finally add and save the university.
		 */
		testimonialService.addTestimonial(testimonial);

		return "redirect:/admin/testimonials";
	}

	/*
	 * Function to delete a testimonial by id
	 */
	@GetMapping("/admin/testimonial/delete/{id}")
	public String deleteTestimonial(@PathVariable long id) {

		if (testimonialRepository.findById(id).isPresent()) {
			deleteImageFromStaticFolderContainingTestimonialImages(id);
			testimonialService.removeTestimonialById(id);
		}

		else {
			//System.out.println("Cannot Delete the object, Later to be displayed over the page");

		}

		return "redirect:/admin/testimonials";
	}

	/*
	 * Function to delete the image from the server before it can be deleted from
	 * the database...
	 */

	public void deleteImageFromStaticFolderContainingTestimonialImages(@PathVariable long id) {

		Testimonial testimonialImageToBeDeleted = testimonialService.getTestimonialById(id).get();
		String myFile = testimonialImageToBeDeleted.getPicFileName();

		/*
		 * Give the exact path where the file is located followed by a slash and then
		 * use the service method of get University by ID
		 */

		File file = new File(fileUtils.getResourcePath("resources.images.testimonialPersonUploadDir") + File.separator + myFile);
//		//System.out.println(file.getAbsolutePath());

		/*
		 * Check if the file exist before deleting and after deleting
		 */
		if (file.exists())
			file.delete();
	}

	@GetMapping("/admin/testimonial/update/{id}")
	public String updateTestimonial(@PathVariable long id, Model model) {
		Testimonial testimonial = testimonialService.getTestimonialById(id).get();
		TestimonialDTO testimonialDTO = new TestimonialDTO();

		testimonialDTO.setId(testimonial.getId());
		testimonialDTO.setName(testimonial.getName());
		testimonialDTO.setInfo(testimonial.getInfo());
		testimonialDTO.setMessage(testimonial.getMessage());
		testimonialDTO.setPicFileName(testimonial.getPicFileName());

		model.addAttribute("testimonialDTO", testimonialDTO);
//		deleteImageFromStaticFolder(id);
		return "testimonialsAdd";
	}

	/*
	 * -------Functions handelling testimonials ends here
	 */

	/*
	 * function to export the list of customers in CSV
	 */
	@Autowired
	CustomerService customerService;

	@GetMapping("/admin/customers/csv")
	public void exportCustomersToCSV(HttpServletResponse response) throws IOException {
		List<Customer> listOfCustomers = customerService.getAllCustomers();
		CustomersCSVExporter exporter = new CustomersCSVExporter();
		exporter.export(listOfCustomers, response);
	}

	/*
	 * function to export the list of institutes in CSV
	 */
	@Autowired
	InstituteService instituteService;

	@GetMapping("/admin/institutes/csv")
	public void exportInstitutesToCSV(HttpServletResponse response) throws IOException {
		List<Institute> listOfInstitutes = instituteService.getAllInstitutes();
		InstitutesCSVExporter exporter = new InstitutesCSVExporter();
		exporter.export(listOfInstitutes, response);
	}

	@Autowired
	MediaService mediaService;

	@GetMapping("/admin/mediaLeads/csv")
	public void exportMediaLeadsToCSV(HttpServletResponse response) throws IOException {
		List<Media> listOfMediaLeads = mediaService.getAllMediaLeads();

		MediaLeadsCSVExporter exporter = new MediaLeadsCSVExporter();
		exporter.export(listOfMediaLeads, response);
	}

	@ResponseBody
	@GetMapping("/admin/api/getAllPagesUrls")
	public ResponseEntity<?> getAllAddedPageUrls() {
		List<String[]> allUrisForPages = pageService.getAllUrisForPages();
		return ResponseEntity.ok(allUrisForPages);
	}

	/*
	 * Test
	 * 
	 */

/*	private URI loadResource(String fileName){
		var file = null;
		try {
			file = FileUtils.filePathFromClassPath(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}*/
//	End of class
}