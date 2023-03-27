from configparser import ConfigParser
from utils import FileUtil
from utils.SftpUtil import SftpUtil

if __name__ == "__main__":
    cfg = ConfigParser()
    sftp = SftpUtil('../sftpConfig.ini')
    sftp.uploadFile(FileUtil.getAllFiles(cfg.get('sftp', 'localpath')))
