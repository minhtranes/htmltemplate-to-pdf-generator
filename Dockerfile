FROM amazoncorretto:11
COPY target/htmltemplate-to-pdf-generator /app/htmltemplate-to-pdf-generator
WORKDIR /app/htmltemplate-to-pdf-generator
ENTRYPOINT ["/bin/sh", "run.sh"]