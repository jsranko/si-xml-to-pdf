**FREE

//*************************************************************************
// Programm : SIXTPT
// Copyright(c) MSIG Europe
//
// Verfasser: Sranko Informatik, Juraj Sranko
// Datum    : 01.04.2021
// énderung :   .  .
//
// Compile  : Z0
//
//*************************************************************************
//
ctl-opt main(main);
ctl-opt dftactgrp(*no) actgrp(*new);
ctl-opt bnddir('$(LIB_SIXTP)/SIXTP');

/include $(INC_SIXTP)/qcpylesrc/sixtpp

//---Main-Procedure--------------------------------------------------------

dcl-proc main;
dcl-pi main;
end-pi;
dcl-s serverXTP like(tSIXTP);
dcl-s htmlData like(tSIXTP_HtmlData);
dcl-s templateData like(tSIEMAIL_TemplateData);

  serverXTP = SIXTP_new('http://192.168.99.130:10033/si-xml-to-pdf-restApi-2.4.3');
  templateData = '{\"title\": \"Tainas Monitor\",\"data\": [{\"Id\": 88070,\"DocumentId\": null,+
                   \"ProgramLibrary\": \"MIPGMTEST \",\"Program\": \"MULTICRB\", +
                   \"ProgramType\": \"*PGM\",\"MessageId\": \"LastDiagMessageText\", +
                   \"MessageText\": \"SQL-Message: Das XML-Parsing oder die öberprÅfung ist \",+
                   \"MessageData\": {\"message\": \"Das XML-Parsing oder die öberprÅfung ist\"},+
                   \"Severity\": 50,\"Created\": \"2021-03-25-13.58.49.641491\",+
                   \"User\": \"TESTJUSR\"},{\"Id\": 88071,\"DocumentId\": null,+
                   \"ProgramLibrary\": \"MIPGMTEST \",\"Program\": \"MULTICRB\", +
                   \"ProgramType\": \"*PGM\",\"MessageId\": \"XMLFileValidateError\",+
                   \"MessageText\": \"XML-Datei /wprints/DEU/CC/Claim-Multicash/x.xml\",+
                   \"Message Data\": {\"file\": \"/wprints/DEU/CC/Claim-Multicash/x.xml\",+
                   \"message\": \"Das XML-Parsing oder die öberprÅfung ist fehlgeschlagen.\"},+
                   \"Severity\": 50,\"Created\": \"2021-03-25-13.58.49.643192\",+
                   \"User\": \"TESTJUSR\"}]}';
  htmlData = SIXTP_getHTML(serverXTP
                          :'/home/testjusr/freemarker/tainas_monitor.html'
                          :templateData
                          :'test_sixtp.html'
                          );

  return;
end-proc;


