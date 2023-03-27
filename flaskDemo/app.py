from flask import Flask
import requests
from configparser import ConfigParser

from utils import FileUtil
from utils.SftpUtil import SftpUtil

cfg = ConfigParser()

app = Flask(__name__)


@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'


@app.route('/sftp', methods='POST')
def callSftp():
    sftp = SftpUtil('../config.ini')
    sftp.uploadFile(FileUtil.getAllFiles(cfg.get('sftp', 'localpath')))
    return None


def testSpringDemo():
    tgt = cfg.get('sftp', 'hostname') + ':' + cfg.get('sftp', 'port') + '/test'
    return requests.get(tgt).content


def testSftpSprintDemo():
    tgt = cfg.get('sftp', 'hostname') + ':' + cfg.get('sftp', 'port') + '/sftp'
    return requests.get(tgt).content


if __name__ == '__main__':
    app.run(host="localhost", port=12138, debug=True)
