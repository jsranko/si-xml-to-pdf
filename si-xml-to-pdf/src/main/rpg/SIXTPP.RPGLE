**FREE

/if not defined(SIXTPP)
  /define SIXTPP
/else
  /eof
/endif

//==========================================================================================
// Constant
//==========================================================================================

//==========================================================================================
// Templates
//==========================================================================================

dcl-ds tSIXTP likeds(tSIXTP_Def);
dcl-s tSIXTP_ServerUrl varchar(512) template;
dcl-s tSIXTP_PathToTemplate varchar(256) template;
dcl-s tSIEMAIL_TemplateData varchar(1000000) template;
dcl-s tSIEMAIL_RequestData pointer template;
dcl-s tSIXTP_PDFData like(tSIEMAIL_RequestData) template;
dcl-s tSIXTP_HtmlData like(tSIEMAIL_RequestData) template;
dcl-s tSIXTP_ResponseSize int(10) template;

dcl-ds tSIXTP_Def qualified;
  SeverUrl like(tSIXTP_ServerUrl);
  ResponseSize like(tSIXTP_ResponseSize);
end-ds;

/if defined(SIXTP)
  /eof
/endif

//==========================================================================================
// Prototypes
//==========================================================================================

dcl-pr SIXTP_new like(tSIXTP) extproc(*dclcase);
  SeverUrl like(tSIXTP_ServerUrl) const options(*varsize);
end-pr;

dcl-pr SIXTP_getHTML like(tSIXTP_HtmlData) extproc(*dclcase);
  SIXTP like(tSIXTP);
  PathToTemplate pointer const options(*string);
  TemplateData pointer const options(*string);
  PathToOutFile pointer const options(*string:*nopass);
end-pr;

dcl-pr SIXTP_getPDF like(tSIXTP_PDFData) extproc(*dclcase);
  SIXTP like(tSIXTP);
  PathToTemplate pointer const options(*string);
  TemplateData pointer const options(*string);
  PathToOutFile pointer const options(*string:*nopass);
end-pr;

dcl-pr SIXTP_close extproc(*dclcase);
  SeverUrl like(tSIXTP_ServerUrl) const options(*varsize);
end-pr;
