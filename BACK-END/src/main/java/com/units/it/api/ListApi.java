package com.units.it.api;

import com.units.it.entities.AccountProxy;
import com.units.it.helpers.AccountHelper;
import com.units.it.helpers.FileHelper;
import com.units.it.helpers.ListHelpers;
import com.units.it.security.Jwt;
import com.units.it.entities.Account;
import com.units.it.entities.File;
import com.units.it.entities.FileProxy;
import com.units.it.utils.ReportAdmin;
import com.units.it.utils.Period;
import com.units.it.utils.Consts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/list")
public class ListApi {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    /*
     * https://stackoverflow.com/questions/47327162/messagebodywriter-not-found-for-media-type-application-xml
     */

    /**
     * Uploaders web service
     *
     * @return return the proxy List of uploaders or a response BAD_REQUEST
     */
    @GET
    @Path("/uploaders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUploaders() {
        try {
            //instantiate the array list
            List<Account> uploaders = new ArrayList<>();

            //retrieve the token to obtain the consumer username
            String token = Jwt.getTokenJWTFromRequest(request);
            String usernameConsumer = Jwt.getUsernameFromJWT(token);

            /*Retrieve the file that belong to the consumer from the db and for each file
            contained in the list, retrieve the uploader username from the db and if it si not already
            present in the uploader list add it
             */
            List<File> dbFilesConsumer = FileHelper.consumerFileList(usernameConsumer);
            for (File file : dbFilesConsumer) {
                Account a = AccountHelper.getById(Account.class, file.getUsernameUpl());
                if (!uploaders.contains(a))
                    uploaders.add(a);
            }
            return Response
                    .status(Response.Status.OK)
                    .entity(new GenericEntity<List<AccountProxy>>(ListHelpers.getAccountProxyListFromAccounts(uploaders)){})
                    .build();
        } catch (Exception e) {
            if (Consts.debug){
                System.out.println(e.getMessage() + "\n");
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

    /**
     * Consumer/Files web service
     *
     * @return FileProxylist of a specific consumer or a response BAD_REQUEST
     */
    @GET
    @Path("/consumer/files")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilesConsumer() {
        try {
            //retrieve the token to obtain the consumer username
            String token = Jwt.getTokenJWTFromRequest(request);
            String usernameConsumer = Jwt.getUsernameFromJWT(token);
            return Response
                    .status(Response.Status.OK)
                    .entity(new GenericEntity<List<FileProxy>>(ListHelpers.consumerFilesProxy(usernameConsumer)) {})
                    .build();
        } catch (Exception e) {
            if (Consts.debug) {
                System.out.println(e.getMessage() + "\n");
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

    /**
     * Consumers web service
     *
     * @return the proxy list of the consumers or response BAD_REQUEST
     */
    @GET
    @Path("/consumers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConsumers() {
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(new GenericEntity<List<AccountProxy>>(ListHelpers.accountProxyListByRole(Consts.CONSUMER)) {})
                    .build();
        } catch (Exception e) {
            if (Consts.debug){
                System.out.println(e.getMessage() + "\n");
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

    /**
     * Uploader/ files web service
     *
     * @return the proxy list of files loaded by an uploader or response BAD_REQUEST
     */
    @GET
    @Path("/uploader/files")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilesUploader() {
        try {
            //retrieve the token to obtain the username of the consumer
            String token = Jwt.getTokenJWTFromRequest(request);
            String usernameUploader = Jwt.getUsernameFromJWT(token);

            if(Consts.debug){
                System.out.println("username uploader from the path uploader/files: "+ usernameUploader);
            }

            return Response
                    .status(Response.Status.OK)
                    .entity(new GenericEntity<List<FileProxy>>(ListHelpers.uploaderFilesProxy(usernameUploader)) {})
                    .build();
        } catch (Exception e) {
            if (Consts.debug){
                System.out.println(e.getMessage() + "\n");
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

    /**
     * Admin/report web service
     *
     * @param timeInterval in which we want to create the report
     * @return response containing the report admin or BAD_REQUEST
     */
    @POST
    @Path("/admin/report")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFiles(Period timeInterval) {
        try {
            //retrieve the 2 period bounds from the timeInterval
            Date from = new SimpleDateFormat("yyyy-MM-dd").parse(timeInterval.getFrom());
            Date to = new SimpleDateFormat("yyyy-MM-dd").parse(timeInterval.getTo());


            //retrieve from the db the uploader proxy list and the file proxy list
            List<AccountProxy> uploaderProxyList = ListHelpers.accountProxyListByRole(Consts.UPLOADER);
            List<FileProxy> completeFilesProxyList = ListHelpers.completeFilesProxy();

            //create an ArrayList of report admin that will be returned in the response
            List<ReportAdmin> reportAdminList = new ArrayList<>();
            if (Consts.debug) {
                System.out.println("-----------REPORT--------------");
            }

            for (AccountProxy uploaderProxy : uploaderProxyList) {
                /*for each uploader proxy we set a list of consumers to know
                for which consumer the uploader load the files
                 */
                ArrayList<String> consumers = new ArrayList<>();

                if (Consts.debug) {
                    System.out.println("USERNAME " + uploaderProxy.getUsername());
                }

                /*setting the iterator to loop through completeFilesProxyList and the atomic counter for the
                number of file uploaded
                 */
                Iterator<FileProxy> iterator = completeFilesProxyList.iterator();
                AtomicInteger fileUploadedCounter = new AtomicInteger();

                /*until there are more elements in completeFilesProxyList take the single file element
                and check if the username of the uploader is equal to the field usernameUpl contained in file
                 */
                while (iterator.hasNext()) {
                    FileProxy file = iterator.next();
                    if (uploaderProxy.getUsername().equals(file.getUsernameUpl())) {
                        //convert the string dateupload from the file object into a date object to perform different controls
                        Date fileDate = new SimpleDateFormat("yyyy-MM-dd").parse(file.getDataUpload().substring(0, 10));

                        //if date is in the range of the time interval we upload the counter of the uploaded files
                        if ((fileDate.after(from) || fileDate.equals(from)) && fileDate.before(to)) {
                            fileUploadedCounter.incrementAndGet();

                            //if the list of consumer don't contain the consumer username contained in the file we add it to the list
                            if (!consumers.contains(file.getUsernameCons())) {
                                consumers.add(file.getUsernameCons());
                            }
                        }
                        //if the date is not in the correct range remove the file from the FilesProxyList
                        iterator.remove();
                    }
                }
                if (Consts.debug) {
                    System.out.println("End for " + uploaderProxy.getUsername() + "-#files " + fileUploadedCounter.get() + " -#consumers " + consumers.size());
                }
                //adding to the reportAdminlist a new report admin element
                reportAdminList.add(new ReportAdmin(uploaderProxy.getUsername(), uploaderProxy.getName(), uploaderProxy.getEmail(), fileUploadedCounter.get(), consumers.size(),uploaderProxy.getLogo()));
            }

            return Response
                    .status(Response.Status.OK)
                    .entity(new GenericEntity<List<ReportAdmin>>(reportAdminList) {})
                    .build();
        } catch (Exception e) {
            if (Consts.debug){
                System.out.println(e.getMessage() + "\n");
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

    /**
     * Administrators web service
     *
     * @return the list of the amdinistrators or BAD_REQUEST
     */
    @GET
    @Path("/administrators")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdministrators() {
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(new GenericEntity<List<AccountProxy>>(ListHelpers.accountProxyListByRole(Consts.ADMINISTRATOR)) {})
                    .build();
        } catch (Exception e) {
            if (Consts.debug) {
                System.out.println(e.getMessage() + "\n");
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

}

