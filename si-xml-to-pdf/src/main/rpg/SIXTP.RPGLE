**FREE

//******************************************************************************************
//
//
// Programname    : SIXTP
// Programautor   : Sranko Informatik
// Created on     : 01.04.2021
//
//==========================================================================================
//             Program-Compiling
//==========================================================================================
//
// See makefile
//
//==========================================================================================

ctl-opt NoMain;
ctl-opt bnddir('$(LIB_SIIIA)/SIIIA');
 //ctl-opt stgmdl(*teraspace);

//==========================================================================================
// Constant
//==========================================================================================

//==========================================================================================
// Templates
//==========================================================================================


//==========================================================================================
// Prototypes
//==========================================================================================

/define SIXTP
/include $(INC_SIXTP)/qcpylesrc/sixtpp

/include $(INC_SIXTP)/qcpylesrc/memcpyp
/include $(INC_SIXTP)/qcpylesrc/sisqlp
/include $(INC_SIXTP)/qcpylesrc/sijlp
/include $(INC_SIXTP)/qcpylesrc/systemp

//==========================================================================================
// Procedures
//==========================================================================================

//------------------------------------------------------------------------------------------

dcl-proc SIXTP_new export;
dcl-pi SIXTP_new like(tSIXTP) extproc(*dclcase);
  SeverUrl like(tSIXTP_ServerUrl) const options(*varsize);
end-pi;
dcl-ds dsSIXTP likeds(tSIXTP_Def) inz;

  dsSIXTP.SeverUrl = SeverUrl;
  return dsSIXTP;
end-proc;

//------------------------------------------------------------------------------------------

dcl-proc SIXTP_getHTML export;
dcl-pi SIXTP_getHTML like(tSIXTP_HtmlData) extproc(*dclcase);
  SIXTP like(tSIXTP);
  PathToTemplate pointer const options(*string);
  TemplateData  pointer const options(*string);
  PathToOutFile pointer const options(*string:*nopass);
end-pi;
dcl-s output like(tSIXTP_HtmlData);
dcl-ds dsSIXTP likeds(tSIXTP_Def) based(pSIXTP);

  pSIXTP = %addr(SIXTP);

  if %parms() >= %parmnum(PathToOutFile);
    output = doRequest(dsSIXTP:'/getHTML':PathToTemplate:TemplateData:PathToOutFile);
  else;
    output = doRequest(dsSIXTP:'/getHTML':PathToTemplate:TemplateData);
  endif;

  return output;
end-proc;

//------------------------------------------------------------------------------------------

dcl-proc SIXTP_getPDF export;
dcl-pi SIXTP_getPDF like(tSIXTP_PDFData) extproc(*dclcase);
  SIXTP like(tSIXTP);
  PathToTemplate pointer const options(*string);
  TemplateData pointer const options(*string);
  PathToOutFile pointer const options(*string:*nopass);
end-pi;
dcl-s output like(tSIXTP_PDFData);
dcl-ds dsSIXTP likeds(tSIXTP_Def) based(pSIXTP);

  pSIXTP = %addr(SIXTP);

  if %parms() >= %parmnum(PathToOutFile);
    output = doRequest(dsSIXTP:'/getPDF':PathToTemplate:TemplateData:PathToOutFile);
  else;
    output = doRequest(dsSIXTP:'/getPDF':PathToTemplate:TemplateData);
  endif;

  return output;
end-proc;

//------------------------------------------------------------------------------------------

dcl-proc SIXTP_close export;
dcl-pi SIXTP_close extproc(*dclcase);
  SeverUrl like(tSIXTP_ServerUrl) const options(*varsize);
end-pi;
dcl-ds dsSIXTP likeds(tSIXTP_Def) inz;

  clear dsSIXTP;
  return;
end-proc;

//------------------------------------------------------------------------------------------

dcl-proc doRequest;
dcl-pi doRequest like(tSIEMAIL_RequestData);
  dsSIXTP likeds(tSIXTP_Def);
  Url varchar(50) const;
  PathToTemplate pointer const options(*string);
  TemplateData pointer const options(*string);
  PathToOutFile pointer const options(*string:*nopass);
end-pi;

dcl-s AppUrl like(tSIXTP_ServerUrl);
dcl-s HttpHeader sqltype(clob:100000);
dcl-s HttpBodyChar varchar(2000000);
dcl-s HttpBodyCharU8 varchar(2000000) ccsid(*utf8);
dcl-s HttpBody sqltype(clob:2000000) ccsid(*utf8);
dcl-s HttpResponse sqltype(blob:4000000);
dcl-s PathToTpl like(tSIXTP_PathToTemplate);
dcl-s PathToFile like(tSIXTP_PathToTemplate);
dcl-s saveToFile ind;
dcl-s output like(tSIEMAIL_RequestData);

  if %parms() >= %parmnum(PathToOutFile);
     saveToFile = *on;
     PathToFile = %str(PathToOutFile);
  endif;

  AppUrl = dsSIXTP.SeverUrl + Url;
  PathToTpl = %str(PathToTemplate);

  HttpBodyChar = %str(TemplateData);
  HttpBodyCharU8 = HttpBodyChar;
  HttpBody_Len = %len(HttpBodyCharU8);
  memcpy(%addr(HttpBody_Data):%addr(HttpBodyCharU8:*data):%len(HttpBodyCharU8));

  exec sql set :HttpHeader =
               '<httpHeader>' Concat
                   '<header name="Content-Type" ' Concat
                   'value="application/json"/>' Concat '</httpHeader>';

  exec sql set :HttpBody = json_object(key 'template' value :PathToTpl,
                                       key 'datenTyp' value 'json',
                                       key 'data' value :HttpBody
                           );

//  exec sql set :HttpBody = '{' concat
//                              '"template":"' concat :PathToTpl concat '",' concat
//                              '"datenTyp":"json",' concat
//                              '"data":"' concat :HttpBody concat '"' concat
//                           '}';
  exec sql set :HttpResponse = systools.httppostblob(:AppUrl, :HttpHeader, :HttpBody);
  if SISQL_isError(sqlca);
    return *null;
  endif;

  if saveToFile;
    exec sql call SaveBlobToFile(:PathToFile, :HttpResponse);
    if SISQL_isError(sqlca);
       SIJL_write('BLOB-File %s kann nicht erstellt werden':PathToFile);
       return *null;
    endif;
    system('CHGATR OBJ(''' + PathToFile + ''') ATR(*CCSID) VALUE(1208)');
  endif;

  dsSIXTP.ResponseSize = HttpResponse_Len;
  output = %addr(HttpResponse_Data);
  return output;
end-proc;


