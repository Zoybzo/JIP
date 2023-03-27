from flask import Flask
import requests
from configparser import ConfigParser

from utils import FileUtil
from utils.SftpUtil import SftpUtil

cfg = ConfigParser()

app = Flask(__name__)


@app.route('/')
def hello_world():  # put application's code here
    print('hello_world')
    return 'Hello World!'


@app.route('/sftp', methods=['GET'])
def callSftp():
    sftp = SftpUtil('config/sftpConfig.ini')
    sftp.uploadFile(FileUtil.getAllFiles(cfg.get('sftp', 'localpath')))
    return "sftp succeed"


def testSpringDemo():
    tgt = cfg.get('sftp', 'hostname') + ':' + cfg.get('sftp', 'port') + '/test'
    return requests.get(tgt).content


def testSftpSprintDemo():
    tgt = cfg.get('sftp', 'hostname') + ':' + cfg.get('sftp', 'port') + '/sftp'
    return requests.get(tgt).content


if __name__ == '__main__':
    app.run(host=cfg.get('server', 'hostname'), port=cfg.getint('server', 'port'), debug=True)
