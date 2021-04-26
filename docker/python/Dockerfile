FROM python:3.7

WORKDIR /app
COPY src/requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY src/server.py /app
COPY src/config.py /app
COPY src/helper.py /app

ENTRYPOINT [ "python" ]
CMD ["server.py"]