package com.smartit.jobSeeker.httpRetrofit;

import com.smartit.jobSeeker.apiResponse.EditKeySkillsAndUpdate.EditKeySkillAndUpdate;
import com.smartit.jobSeeker.apiResponse.addedJobAlert.AddedJobAlert;
import com.smartit.jobSeeker.apiResponse.applicationHistory.appliedJobs.AppliedJobsForAppHistory;
import com.smartit.jobSeeker.apiResponse.applicationHistory.applyFilter.ApplyFilter;
import com.smartit.jobSeeker.apiResponse.applyInterviewQuestions.ApplyInterviewQuestion;
import com.smartit.jobSeeker.apiResponse.changePassword.ChangePassword;
import com.smartit.jobSeeker.apiResponse.chatApiResponses.CloseApiRequest;
import com.smartit.jobSeeker.apiResponse.chatApiResponses.EOChatHistory;
import com.smartit.jobSeeker.apiResponse.chatApiResponses.EOSendMessage;
import com.smartit.jobSeeker.apiResponse.currencyType.CurrencyType;
import com.smartit.jobSeeker.apiResponse.degreeType.DegreeType;
import com.smartit.jobSeeker.apiResponse.delete_education.DeleteEducationDetail;
import com.smartit.jobSeeker.apiResponse.delete_employment.DeleteEmploymentDetail;
import com.smartit.jobSeeker.apiResponse.displayEditJob.DisplayEditJob;
import com.smartit.jobSeeker.apiResponse.editAddressDetails.EditAddressDetails;
import com.smartit.jobSeeker.apiResponse.editOtherDetails.EditOthersDetails;
import com.smartit.jobSeeker.apiResponse.editSalaryDetails.EditSalaryDetails;
import com.smartit.jobSeeker.apiResponse.edit_profile_summary.EditProfileSummary;
import com.smartit.jobSeeker.apiResponse.forgotPassword.ForgotPassword;
import com.smartit.jobSeeker.apiResponse.gerProfileSkills.GetProfileSkills;
import com.smartit.jobSeeker.apiResponse.getAudioResume.GetAudioResume;
import com.smartit.jobSeeker.apiResponse.getCity.GetCity;
import com.smartit.jobSeeker.apiResponse.getContractType.ContractType;
import com.smartit.jobSeeker.apiResponse.getCountry.Country;
import com.smartit.jobSeeker.apiResponse.getEducationDetails.GetEducationDetails;
import com.smartit.jobSeeker.apiResponse.getEducationDetailsForParticularJob.GetEduDetailsParticular;
import com.smartit.jobSeeker.apiResponse.getEmploymentDetails.GetEmploymentDetails;
import com.smartit.jobSeeker.apiResponse.getEmploymentDetailsForParticularJob.GetEmpDetailsParticular;
import com.smartit.jobSeeker.apiResponse.getFunctionalArea.GetFunctionalArea;
import com.smartit.jobSeeker.apiResponse.getPhoneCode.GetPhoneCode;
import com.smartit.jobSeeker.apiResponse.getProfileDetail.GetProfileDetails;
import com.smartit.jobSeeker.apiResponse.getResumeInfo.GetResumeInfo;
import com.smartit.jobSeeker.apiResponse.getState.State;
import com.smartit.jobSeeker.apiResponse.handShakeForDashboard.HandshakeForDashboard;
import com.smartit.jobSeeker.apiResponse.industry.Industry;
import com.smartit.jobSeeker.apiResponse.interviewFeedback.InteeviewFeedback;
import com.smartit.jobSeeker.apiResponse.interview_list_mode.InterviewListMode;
import com.smartit.jobSeeker.apiResponse.inviationModule.InvitationModule;
import com.smartit.jobSeeker.apiResponse.invitationAccept.InvitationAccept;
import com.smartit.jobSeeker.apiResponse.invitationReject.InvitationReject;
import com.smartit.jobSeeker.apiResponse.invitationRescheduled.InviationRescheduled;
import com.smartit.jobSeeker.apiResponse.jobAlert.JobAlert;
import com.smartit.jobSeeker.apiResponse.jobDescription.JobDescription;
import com.smartit.jobSeeker.apiResponse.jobDescriptionNew.JobDescriptionNew;
import com.smartit.jobSeeker.apiResponse.liveInterview.EOLiveInterview;
import com.smartit.jobSeeker.apiResponse.mobDetail.MobDetail;
import com.smartit.jobSeeker.apiResponse.notInterested.NotInterested;
import com.smartit.jobSeeker.apiResponse.preRecordedInterviewKit.PreRecordedInterviewKit;
import com.smartit.jobSeeker.apiResponse.privacySettings.PrivacySettings;
import com.smartit.jobSeeker.apiResponse.profileVideoForFirstTime.ProfileVideo;
import com.smartit.jobSeeker.apiResponse.qrCode.QRCode;
import com.smartit.jobSeeker.apiResponse.recordedVideo.RecordedVideo;
import com.smartit.jobSeeker.apiResponse.retake.Retake;
import com.smartit.jobSeeker.apiResponse.salaryType.SalaryType;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.functional_area.GetFunctionalAreaRequest;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.job_search.SearchJobRequest;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.job_type.GetJobTypeRequest;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.salary_type.GetSalaryRequest;
import com.smartit.jobSeeker.apiResponse.signIn.SignIn;
import com.smartit.jobSeeker.apiResponse.signUp.SignUp;
import com.smartit.jobSeeker.apiResponse.socialLogin.SocialLoginFB;
import com.smartit.jobSeeker.apiResponse.submitInterviewAnswer.InterviewAnswer;
import com.smartit.jobSeeker.apiResponse.timeZone.TimeZone;
import com.smartit.jobSeeker.apiResponse.reportAnIssue.ReportAnIssue;
import com.smartit.jobSeeker.apiResponse.trackReportedIssue.TrackReportedIssue;
import com.smartit.jobSeeker.apiResponse.updateBasicProfile.UpdateBasicProfile;
import com.smartit.jobSeeker.apiResponse.updateEducation.UpdateEducation;
import com.smartit.jobSeeker.apiResponse.updateEmployment.UpdateEmployment;
import com.smartit.jobSeeker.apiResponse.updatePrivacySettings.UpdatePrivacySettings;
import com.smartit.jobSeeker.apiResponse.updatedJobAlert.UpdatedJobAlert;
import com.smartit.jobSeeker.apiResponse.uploadAudioResume.UploadAudioResume;
import com.smartit.jobSeeker.apiResponse.uploadProfilePicture.UploadProfile;
import com.smartit.jobSeeker.apiResponse.uploadResume.UploadResume;
import com.smartit.jobSeeker.apiResponse.visaStatus.VisaStatus;
import com.smartit.jobSeeker.atsApiResponses.atsRetake.AtsRetake;
import com.smartit.jobSeeker.atsApiResponses.ats_deeplink.deeplinking_url.AtsDeeplink;
import com.smartit.jobSeeker.atsApiResponses.ats_total_question.AtsTotalInteviewQuestion;
import com.smartit.jobSeeker.atsApiResponses.feedback_for_ats.FeedbackForAts;
import com.smartit.jobSeeker.atsApiResponses.submit_answer_essay_for_ats.EssaySubmitAnsForAts;
import com.smartit.jobSeeker.atsApiResponses.submit_answer_mcq_for_ats.MCQSubmitAnsForAts;
import com.smartit.jobSeeker.atsApiResponses.submit_answer_video_for_ats.VideoSubmitAnsForAts;
import com.smartit.jobSeeker.internal_deeplinkingApiResponse.InternalDeeplinking;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RemoteRepositoryService {


    // SIGN UP

    @FormUrlEncoded
    @POST("jobseeker/signup")
    Observable<SignUp> signUpForJobSeeker(@Field("firstname") String firstname,
                                          @Field("lastname") String lastname,
                                          @Field("email") String email,
                                          @Field("phonecode") String phonecode,
                                          @Field("phone") String phone,
                                          @Field("password") String password);


    // SIGN IN

    @FormUrlEncoded
    @POST("jobseeker/login")
    Observable<SignIn> signInForJobSeeker(@Field("email") String email,
                                          @Field("password") String password);


    // FORGET PASSWORD

    @FormUrlEncoded
    @POST("jobseeker/forgot-password")
    Observable<ForgotPassword> forgotPasswordForJobSeeker(@Header("Authorization") String Authorization,
                                                          @Field("email") String forgot_password);


    // GET PHONE CODE

    @GET("jobseeker/get-phone-code")
    Observable<GetPhoneCode> getPhoneCodeForJobSeeker();


    // HAND-SHAKE FOR DASHBOARD
    @GET("jobseeker/handshake-request")
    Call<HandshakeForDashboard> handShakeForJobSeeker(@Header("Authorization") String Authorization);


    // INTERVIEW LIST MODE

    @FormUrlEncoded
    @POST("jobseeker/interview-list")
    Call<InterviewListMode> interviewListModeForJobSeeker(@Header("Authorization") String Authorization,
                                                          @Field("offset") String offset,
                                                          @Field("limit") Integer limit);


    // INVITATION MODULE

    @FormUrlEncoded
    @POST("jobseeker/invitations")
    Call<InvitationModule> inviationForJobSeeker(@Header("Authorization") String Authorization,
                                                 @Field("offset") String offset,
                                                 @Field("limit") Integer limit);


    // INVITATION MODULE ACCEPT

    @FormUrlEncoded
    @POST("jobseeker/invitation-apply")
    Call<InvitationAccept> invitationAcceptForJobSeeker(@Header("Authorization") String Authorization,
                                                        @Field("invitation_id") Integer invitation_id,
                                                        @Field("apply") Integer apply);

    // INVITATION MODULE REJECT

    @FormUrlEncoded
    @POST("jobseeker/invitation-apply")
    Call<InvitationReject> invitationRejectForJobSeeker(@Header("Authorization") String Authorization,
                                                        @Field("invitation_id") Integer invitation_id,
                                                        @Field("apply") Integer apply);


    // INVITATION MODULE RESCHEDULE

    @FormUrlEncoded
    @POST("jobseeker/invitation-apply")
    Call<InviationRescheduled> invitationRescheduledForJobSeeker(@Header("Authorization") String Authorization,
                                                                 @Field("invitation_id") Integer invitation_id,
                                                                 @Field("apply") Integer apply,
                                                                 @Field("invitation_date") String invitation_date,
                                                                 @Field("start_time") String start_time,
                                                                 @Field("end_time") String end_time,
                                                                 @Field("timezone") String timezone,
                                                                 @Field("message") String message);
    // TIME ZONE LIST

    @GET("jobseeker/time-zone-list")
    Call<TimeZone> timeZoneForJobSeeker();


    // APPLICATION HISTORY (APPLIED JOBS)

    @FormUrlEncoded
    @POST("jobseeker/appliedjobs")
    Call<AppliedJobsForAppHistory> appliedJobsForJobSeeker(@Header("Authorization") String Authorization,
                                                           @Field("offset") Integer offset,
                                                           @Field("limit") Integer limit,
                                                           @Field("status") String status);


    // APPLICATION HISTORY (FILTER JOBS)

    @FormUrlEncoded
    @POST("jobseeker/appliedjobs")
    Call<ApplyFilter> applyFilterForJobSeeker(@Header("Authorization") String Authorization,
                                              @Field("offset") Integer offset,
                                              @Field("limit") Integer limit,
                                              @Field("status") String status);


    // APPLICATION HISTORY (FILTER JOBS)

    @FormUrlEncoded
    @POST("jobseeker/appliedjobs")
    Call<AppliedJobsForAppHistory> applyFilterToRefreshDataForJobSeeker(@Header("Authorization") String Authorization,
                                                                        @Field("offset") Integer offset,
                                                                        @Field("limit") Integer limit,
                                                                        @Field("status") String status);


    // PROFILE VIDEO (FOR THE FIRST TIME)

    @GET("jobseeker/video-profile")
    Call<ProfileVideo> profileVideoForJobSeeker(@Header("Authorization") String Authorization);


    // RECORDED VIDEO


    @Multipart
    @POST("jobseeker/uploadvid")
    Call<RecordedVideo> recordedVideo(@Header("Authorization") String Authorization, // String will also take
                                      @Part MultipartBody.Part file);


/*    @Multipart
    @POST("jobseeker/uploadvid")
    Call<RecordedVideo> recordedVideo(@Header("Authorization") String Authorization, // String will also take
                                      @Part MultipartBody.Part file);*/

    // UPLOAD PROFILE IMAGE

    @Multipart
    @POST("jobseeker/upload-image")
    Call<UploadProfile> uploadUserProfile(@Header("Authorization") String Authorization,
                                          @Part MultipartBody.Part file);

    // SOCIAL LOGIN FOR FB


    @FormUrlEncoded
    @POST("jobseeker/social-login")
    Call<SocialLoginFB> socialLoginForJobSeeker(@Header("Authorization") String Authorization,
                                                @Field("firstname") String firstname,
                                                @Field("lastname") String lastname,
                                                @Field("email") String email);


    //  APPLY INTERVIEW (INTERVIEW NOW)

    @FormUrlEncoded
    @POST("jobseeker/interview-question")
    Call<ApplyInterviewQuestion> applyInterviewNowForJobSeeker(@Header("Authorization") String Authorization,
                                                               @Field("job_id") Integer job_id);


    //  APPLY INTERVIEW (INTERVIEW NOW) ------- COPIED(USED)

    @FormUrlEncoded
    @POST("jobseeker/interview-question")
    Call<PreRecordedInterviewKit> applyInterviewNowUsedForJobSeeker(@Header("Authorization") String Authorization,
                                                                    @Field("job_id") Integer job_id);


    // SUBMIT INTERVIEW ANSWER

    @FormUrlEncoded
    @POST("jobseeker/interview-answer")
    Call<InterviewAnswer> submitInterviewAnswerForJobSeeker(@Header("Authorization") String Authorization,
                                                            @Field("qtype") String qtype,
                                                            @Field("ques_id") Integer ques_id,
                                                            @Field("duration") Float duration,
                                                            @Field("answer") String answer,
                                                            @Field("attempt") Integer attempt,
                                                            @Field("invitation_id") String invitation_id,
                                                            @Field("post_id") String post_id);


    // SUBMIT INTERVIEW ANSWER FOR VIDEO

    @Multipart
    @POST("jobseeker/interview-answer")
    Call<InterviewAnswer> submitInterviewAnswerForVideo(@Header("Authorization") String Authorization,
                                                        @Part("qtype") Integer qtype,
                                                        @Part("ques_id") Integer ques_id,
                                                        @Part("duration") Float duration,
                                                        @Part MultipartBody.Part file,
                                                        @Part("attempt") Integer attempt,
                                                        @Part("invitation_id") Integer invitation_id,
                                                        @Part("post_id") Integer post_id);


    @Multipart
    @POST("jobseeker/interview-answer")
    Call<InterviewAnswer> submitInterviewAnswerForVideoForSkip(@Header("Authorization") String Authorization,
                                                               @Part("qtype") Integer qtype,
                                                               @Part("ques_id") Integer ques_id,
                                                               @Part("duration") Float duration,
                                                               @Part("video_answer") String file,
                                                               @Part("attempt") Integer attempt,
                                                               @Part("invitation_id") Integer invitation_id,
                                                               @Part("post_id") Integer post_id);


    // RETAKE

    @FormUrlEncoded
    @POST("jobseeker/retake")
    Call<Retake> retakeForJobSeeker(@Header("Authorization") String Authorization,
                                    @Field("post_id") String post_id,
                                    @Field("ques_id") Integer ques_id,
                                    @Field("attempt") Integer attempt,
                                    @Field("invitation_id") String invitation_id,
                                    @Field("qtype") Integer qtype);


    // ATS

    // ATS DEEPLINKING FROM MAIL

    @FormUrlEncoded
    @POST("integration/question")
    Call<AtsDeeplink> deeplinkATSURL(@Field("url") String url);


    // TOTAL INTERVIEW QUESTION FOR ATS

    @FormUrlEncoded
    @POST("integration/interview-question")
    Call<AtsTotalInteviewQuestion> totalInterviewQuestionForAts(@Field("kitId") Integer kitId, // @Header("Authorization") String Authorization,
                                                                @Field("interviewId") Integer interviewId);


    // INTERVIEW FEEDBACK

    @FormUrlEncoded
    @POST("jobseeker/interview-feedback")
    Call<InteeviewFeedback> interviewFeedback(@Header("Authorization") String Authorization,
                                              @Field("applied_id") String applied_id,
                                              @Field("rating") Integer rating,
                                              @Field("description") String description,
                                              @Field("source") String source);


    // ATS RETAKE

    @FormUrlEncoded
    @POST("integration/retake")
    Call<AtsRetake> retakeForAts(@Field("qtype") String qtype,
                                 @Field("invitation_id") String invitation_id,
                                 @Field("ques_id") Integer ques_id,
                                 @Field("attempt") Integer attempt);


    // ATS SUBMIT ANSWER FOR MCQ

    @FormUrlEncoded
    @POST("integration/answer")
    Call<MCQSubmitAnsForAts> mcqForAts(@Field("qtype") String qtype,
                                       @Field("interviewId") String interviewId,
                                       @Field("answer") Integer answer,
                                       @Field("ques_id") Integer ques_id,
                                       @Field("duration") String duration,
                                       @Field("catcher_id") Integer catcher_id,
                                       @Field("total_question") Integer total_question);


    // ATS SUBMIT ANSWER FOR ESSAY

    @FormUrlEncoded
    @POST("integration/answer")
    Call<EssaySubmitAnsForAts> essayForAts(@Field("qtype") String qtype,
                                           @Field("interviewId") String interviewId,
                                           @Field("answer") String answer,
                                           @Field("ques_id") Integer ques_id,
                                           @Field("duration") String duration,
                                           @Field("catcher_id") Integer catcher_id,
                                           @Field("total_question") Integer total_question);


    // ATS SUBMIT ANSWER FOR VIDEO


    @Multipart
    @POST("integration/answer")
    Call<VideoSubmitAnsForAts> videoForAts(@Part("qtype") Integer qtype,
                                           @Part("duration") Float duration,
                                           @Part("total_question") Integer total_question,
                                           @Part MultipartBody.Part file,
                                           @Part("attempt") Integer attempt,
                                           @Part("catcher_id") Integer catcher_id,
                                           @Part("ques_id") Integer ques_id,
                                           @Part("interviewId") Integer interviewId);


    // ATS SUBMIT ANSWER FOR VIDEO for skip

    @Multipart
    @POST("integration/answer")
    Call<VideoSubmitAnsForAts> videoForAtsForSkip(@Part("qtype") Integer qtype,
                                                  @Part("duration") float duration,
                                                  @Part("total_question") Integer total_question,
                                                  @Part("video_answer") String video_answer,
                                                  @Part("attempt") Integer attempt,
                                                  @Part("catcher_id") Integer catcher_id,
                                                  @Part("ques_id") Integer ques_id,
                                                  @Part("interviewId") Integer interviewId);


    // ATS FEEDBACK

    @FormUrlEncoded
    @POST("integration/feedback")
    Call<FeedbackForAts> feedbackForAts(@Field("invitationId") Integer invitationId,
                                        @Field("rating") Integer rating,
                                        @Field("description") String description,
                                        @Field("source") String source);


    @FormUrlEncoded
    @POST("jobseeker/email-link")
    Call<InternalDeeplinking> internalDeeplinking(@Field("url") String url);


    @FormUrlEncoded
    @POST("jobseeker/mob-detail")
    Call<MobDetail> mobDetail(@Header("Authorization") String Authorization,
                              @Field("model_name") String model_name,
                              @Field("sys_model") String sys_model,
                              @Field("state") String state,
                              @Field("country") String country,
                              @Field("zipcode") String zipcode,
                              @Field("city") String city,
                              @Field("lat") String lat,
                              @Field("os_name") String os_name,
                              @Field("lon") String lon,
                              @Field("os_version") String os_version,
                              @Field("device_token") String device_token);


    // QRCode

    @GET("jobseeker/qrcode")
    Call<QRCode> qrCodeApi(@Header("Authorization") String Authorization);


    // Change Password


    @FormUrlEncoded
    @POST("jobseeker/change-password")
    Call<ChangePassword> changePasswordApi(@Header("Authorization") String Authorization,
                                           @Field("currentpass") String currentpass,
                                           @Field("password") String password,
                                           @Field("confirmpass") String confirmpass);


    // Privacy Settings

    @GET("jobseeker/privacy-settings")
    Call<PrivacySettings> privacySettingsApi(@Header("Authorization") String Authorization);


    // Update Privacy Settings

    @FormUrlEncoded
    @POST("jobseeker/update-privacy-settings")
    Call<UpdatePrivacySettings> updatePrivacySettings(@Header("Authorization") String Authorization,

                                                      @Field("primary_phone") String primary_phone,
                                                      @Field("primary_email") String primary_email,
                                                      @Field("audio_resume") String audio_resume,
                                                      @Field("pdf_resume") String pdf_resume,
                                                      @Field("video_resume") String video_resume,

                                                      @Field("facebook") String facebook,
                                                      @Field("linkedin") String linkedin,
                                                      @Field("twitter") String twitter,
                                                      @Field("quora") String quora,
                                                      @Field("web_privacy") String web_privacy);


    // REPORT AN ISSUE


    @FormUrlEncoded
    @POST("jobseeker/report-issue")
    Call<ReportAnIssue> reportAnIssueApi(@Header("Authorization") String Authorization,
                                         @Field("subject") String subject,
                                         @Field("message") String message);


    // TRACK REPORTED ISSUE

    @FormUrlEncoded
    @POST("jobseeker/issue-list")
    Call<TrackReportedIssue> trackReportedIssue(@Header("Authorization") String Authorization,
                                                @Field("offset") int offset,
                                                @Field("limit") int limit);


    // GET JOB ALERTS

    @GET("jobseeker/get-jobalerts")
    Call<JobAlert> jobAlertApi(@Header("Authorization") String Authorization);


    // DELET JOB ALERTS

    @FormUrlEncoded
    @POST("jobseeker/delete-jobalerts")
    Call<ReportAnIssue> deleteJobAlertsApi(@Header("Authorization") String Authorization,
                                           @Field("id") String id);


    // ADD JOB ALERTS

    @FormUrlEncoded
    @POST("jobseeker/add-jobalerts")
    Call<AddedJobAlert> addJobAlertApi(@Header("Authorization") String Authorization,
                                       @Field("name") String name,
                                       @Field("currency_type") String currency_type,
                                       @Field("minsalary") String minsalary,
                                       @Field("industry") String industry,
                                       @Field("maxsalary") String maxsalary,
                                       @Field("salary_type") String salary_type,
                                       @Field("skills") String skills,
                                       @Field("notification") String notification,
                                       @Field("minexp") String minexp,
                                       @Field("maxexp") String maxexp);


    // EDIT JOB ALERTS

    @FormUrlEncoded
    @POST("jobseeker/edit-jobalerts")
    Call<UpdatedJobAlert> editJobAlertApi(@Header("Authorization") String Authorization,
                                          @Field("name") String name,
                                          @Field("currency_type") String currency_type,
                                          @Field("minsalary") String minsalary,
                                          @Field("industry") String industry,
                                          @Field("maxsalary") String maxsalary,
                                          @Field("salary_type") String salary_type,
                                          @Field("skills") String skills,
                                          @Field("notification") String notification,
                                          @Field("minexp") String minexp,
                                          @Field("maxexp") String maxexp,
                                          @Field("id") String id);


    // INDUSTRY

    @GET("jobseeker/get-industry")
    Call<Industry> industryApi(@Header("Authorization") String Authorization);


    // CURRENCY

    @GET("jobseeker/currency")
    Call<CurrencyType> currencyApi(@Header("Authorization") String Authorization);


    // SALARY TYPE

    @GET("jobseeker/saltype")
    Call<SalaryType> salaryApi(@Header("Authorization") String Authorization);


    // DISPLAY EDIT JOB TYPE


    @FormUrlEncoded
    @POST("jobseeker/job-alert")
    Call<DisplayEditJob> displayEditJobAlertApi(@Header("Authorization") String Authorization,
                                                @Field("job_alert_id") int job_alert_id);


    // GET AUDIO RESUME

    @GET("jobseeker/getaudio-resume")
    Call<GetAudioResume> getAudioResumeApi(@Header("Authorization") String Authorization);


    // UPLOAD RESUME


    @Multipart
    @POST("jobseeker/uploadaudio-resume")
    Call<UploadAudioResume> uploadAudio(@Header("Authorization") String Authorization,
                                        @Part MultipartBody.Part file);


    // GET PROFILE DETAILS

    @GET("jobseeker/get-profile")
    Call<GetProfileDetails> getProfileDetailsApi(@Header("Authorization") String Authorization);


    // GET EDUCATION DETAILS

    @GET("jobseeker/education")
    Call<GetEducationDetails> getEducationDetailsApi(@Header("Authorization") String Authorization);


    // GET EDUCATION DETAILS FOR PARTICULAR

    @FormUrlEncoded
    @POST("jobseeker/get-education")
    Call<GetEduDetailsParticular> getEducationDetailsForParticular(@Header("Authorization") String Authorization,
                                                                   @Field("id") String id);


    // GET SKILLS

    @GET("jobseeker/keyskills")
    Call<GetProfileSkills> getProfileSkillsApi(@Header("Authorization") String Authorization);


    // GET EMPLOYMENT DETAILS

    @GET("jobseeker/empdetails")
    Call<GetEmploymentDetails> getEmploymentDetailsApi(@Header("Authorization") String Authorization);


    // GET EMPLOYMENT DETAILS FOR PARTICULAR JOB

    @FormUrlEncoded
    @POST("jobseeker/get-emp")
    Call<GetEmpDetailsParticular> getEmploymentDetailsForParticularJob(@Header("Authorization") String Authorization,
                                                                       @Field("id") String id);


    // GET RESUME DETAILS

    @GET("jobseeker/get-resume")
    Call<GetResumeInfo> getResumeInfoApi(@Header("Authorization") String Authorization);


    // UPLOAD RESUME

    @Multipart
    @POST("jobseeker/upload-resume")
    Call<UploadResume> uploadResumeApi(@Header("Authorization") String Authorization,
                                       @Part MultipartBody.Part file);


    // DELETE EDUCATION ROW


    @FormUrlEncoded
    @POST("jobseeker/deleteeducation")
    Call<DeleteEducationDetail> deleteEducationApi(@Header("Authorization") String Authorization,
                                                   @Field("edu_id") String edu_id);


    // DELETE EMPLOYMENGT DETAILS


    @FormUrlEncoded
    @POST("jobseeker/delete-empdetails")
    Call<DeleteEmploymentDetail> deleteEmploymentApi(@Header("Authorization") String Authorization,
                                                     @Field("id") String id);


    // EDIT PROFILE SUMMARY

    @FormUrlEncoded
    @POST("jobseeker/edit-profile-summary")
    Call<EditProfileSummary> editProfileSummaryApi(@Header("Authorization") String Authorization,
                                                   @Field("summary") String summary);


    // EDIT SALARY DETAILS

    @FormUrlEncoded
    @POST("jobseeker/edit-profile-summary")
    Call<EditProfileSummary> editSalaryDetailsApi(@Header("Authorization") String Authorization,
                                                  @Field("pay_rate") String pay_rate,
                                                  @Field("ctc") String ctc,
                                                  @Field("expected_ctc") String expected_ctc,
                                                  @Field("currency") String currency);

    // CHAT MODULE


    @FormUrlEncoded
    @POST("jobseeker/chat-history")
    Call<EOChatHistory> chatHistory(@Header("Authorization") String Authorization,
                                    @Field("contact_id") int contact_id,
                                    @Field("offset") int offset,
                                    @Field("limit") String limit);

    // SEND CHAT

    @FormUrlEncoded
    @POST("jobseeker/send-chat")
    Call<EOSendMessage> sendChatMessage(@Header("Authorization") String Authorization,
                                        @Field("jobma_contact_id") int jobma_contact_id,
                                        @Field("message") String message);


    // CLOSE CHAT

    @FormUrlEncoded
    @POST("jobseeker/close-chat")
    Call<CloseApiRequest> closeChat(@Header("Authorization") String Authorization,
                                    @Field("jobma_contact_id") int jobma_contact_id,
                                    @Field("message") String message);

    // EDIT SALARY DETAILS

    @FormUrlEncoded
    @POST("jobseeker/edit-salary-details")
    Call<EditSalaryDetails> editSalaryDetails(@Header("Authorization") String Authorization,
                                              @Field("pay_rate") String pay_rate,
                                              @Field("ctc") String ctc,
                                              @Field("expected_ctc") String expected_ctc,
                                              @Field("currency") String currency);


    // EDIT ADDRESS DETAILS

    @FormUrlEncoded
    @POST("jobseeker/edit-address-details")
    Call<EditAddressDetails> editAddresssDetails(@Header("Authorization") String Authorization,
                                                 @Field("current_location") String current_location,
                                                 @Field("desire_location") String desire_location,
                                                 @Field("relocate") String relocate);


    // EDIT OTHERS DETAILS

    @FormUrlEncoded
    @POST("jobseeker/edit-other-details")
    Call<EditOthersDetails> editOthersDetails(@Header("Authorization") String Authorization,
                                              @Field("functional") String functional,
                                              @Field("industry") String industry,
                                              @Field("emp_status") String emp_status,
                                              @Field("desire_jobtype") String desire_jobtype,
                                              @Field("contract") String contract);


    // GET FUNCTIONAL AREA

    @GET("jobseeker/get-functional-area")
    Call<GetFunctionalArea> getFunctionalArea();


    // GET VISA STATUS

    @GET("jobseeker/visa-status")
    Call<VisaStatus> getVisaStatus();


    // GET CONTRACT

    @GET("jobseeker/contype")
    Call<ContractType> getContract(@Header("Authorization") String Authorization);


    // DEGREE TYPE

    @GET("jobseeker/degtype")
    Call<DegreeType> degreeType(@Header("Authorization") String Authorization);


    // COUNTRY

    @GET("jobseeker/get-country")
    Call<Country> getCountry(@Header("Authorization") String Authorization);


    // STATE

    @FormUrlEncoded
    @POST("jobseeker/get-state")
    Call<State> getState(@Header("Authorization") String Authorization,
                         @Field("country_id") int country_id);


    // CITY

    @FormUrlEncoded
    @POST("jobseeker/get-city")
    Call<GetCity> getCity(@Header("Authorization") String Authorization,
                          @Field("state_id") int state_id);

    // EDIT KEY SKILLS

    @FormUrlEncoded
    @POST("jobseeker/editkeyskills")
    Call<EditKeySkillAndUpdate> editKeySkillUpdate(@Header("Authorization") String Authorization,
                                                   @Field("skills") String skills);

    // UPDATE EDUCATION


    @FormUrlEncoded
    @POST("jobseeker/update-education")
    Call<UpdateEducation> updateEducation(@Header("Authorization") String Authorization,
                                          @Field("degree") String degree,
                                          @Field("edu_level") String edu_level,
                                          @Field("start_date") String start_date,
                                          @Field("end_date") String end_date,
                                          @Field("institute") String institute,
                                          @Field("country") String country,
                                          @Field("state") String state,
                                          @Field("city") String city,
                                          @Field("summary") String summary,
                                          @Field("mode") String mode);


    @FormUrlEncoded
    @POST("jobseeker/update-education")
    Call<UpdateEducation> updateEditEducation(@Header("Authorization") String Authorization,
                                              @Field("degree") String degree,
                                              @Field("edu_level") String edu_level,
                                              @Field("start_date") String start_date,
                                              @Field("end_date") String end_date,
                                              @Field("institute") String institute,
                                              @Field("country") String country,
                                              @Field("state") String state,
                                              @Field("city") String city,
                                              @Field("summary") String summary,
                                              @Field("mode") String mode,
                                              @Field("edu_id") String edu_id);


    // UPDATE EMPLOYMENT

    @FormUrlEncoded
    @POST("jobseeker/update-empdetails")
    Call<UpdateEmployment> updateEmployment(@Header("Authorization") String Authorization,
                                            @Field("title") String title,
                                            @Field("company") String company,
                                            @Field("start_date") String start_date,
                                            @Field("end_date") String end_date,
                                            @Field("city") String city,
                                            @Field("state") String state,
                                            @Field("country") String country,
                                            @Field("desc") String desc,
                                            @Field("industry") String industry,
                                            @Field("mode") String mode);


    // UPDATE EDIT EMPLOYMENT

    @FormUrlEncoded
    @POST("jobseeker/update-empdetails")
    Call<UpdateEmployment> updateEditEmployment(@Header("Authorization") String Authorization,
                                                @Field("title") String title,
                                                @Field("company") String company,
                                                @Field("start_date") String start_date,
                                                @Field("end_date") String end_date,
                                                @Field("city") String city,
                                                @Field("state") String state,
                                                @Field("country") String country,
                                                @Field("desc") String desc,
                                                @Field("industry") String industry,
                                                @Field("mode") String mode,
                                                @Field("id") String id);


    // EDIT BASIC PROFILE

    @FormUrlEncoded
    @POST("jobseeker/edit-basic-profile")
    Call<UpdateBasicProfile> editBasicProfile(@Header("Authorization") String Authorization,
                                              @Field("jobtitle") String jobtitle,
                                              @Field("company") String company,
                                              @Field("exp") String exp,
                                              @Field("expmonth") String expmonth,
                                              @Field("city") String city,
                                              @Field("state") String state,
                                              @Field("country") String country,
                                              @Field("zip") String zip);


    // CHANGES CHARANVEER

    @GET("jobseeker/contype")
    Call<GetJobTypeRequest> getFilterJobType();


    @GET("jobseeker/saltype")
    Call<GetSalaryRequest> getSalaryType();

    @GET("jobseeker/get-functional-area")
    Call<GetFunctionalAreaRequest> getFunctionalAreaFilter();


    @GET("jobseeker/get-industry")
    Call<GetFunctionalAreaRequest> getIndustryType();


    @Headers({"Content-Type: application/json"})
    @POST("jobseeker/jobs-search")
    Call<SearchJobRequest> searchJobs(@Header("Authorization") String Authorization,
                                      @Body RequestBody params);

    @FormUrlEncoded
    @POST("jobseeker/not-interested")
    Call<NotInterested> notInterested(@Header("Authorization") String Authorization,
                                      @Field("job_id") String job_id);


    // JOB DESCRIPTION

    @FormUrlEncoded
    @POST("jobseeker/jobdesc")
    Call<JobDescriptionNew> jobDescription(@Header("Authorization") String Authorization,
                                           @Field("job_id") String job_id,
                                           @Field("invitation_id") String invitation_id);

    @FormUrlEncoded
    @POST("jobseeker/live-interview-check")
    Call<EOLiveInterview> liveInterviewCheck(@Header("Authorization") String key,
                                             @Field("invite_id") int inviteId,
                                             @Field("token") String token);


}
