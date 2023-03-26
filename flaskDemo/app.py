from flask import Flask

app = Flask(__name__)


@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'


@app.route('/sftp', methods='POST')
def callSftp():
    return None


if __name__ == '__main__':
    app.run(host="localhost", port=12138, debug=True)
