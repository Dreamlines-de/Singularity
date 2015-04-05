package com.hubspot.singularity.executor.config;

import org.hibernate.validator.constraints.NotEmpty;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.hubspot.mesos.MesosUtils;
import com.hubspot.singularity.runner.base.constraints.DirectoryExists;
import com.hubspot.singularity.runner.base.config.SingularityRunnerConfiguration;

public class SingularityExecutorConfiguration implements SingularityRunnerConfiguration {
  public static final String SHUTDOWN_TIMEOUT_MILLIS = "executor.shutdown.timeout.millis";

  public static final String HARD_KILL_AFTER_MILLIS = "executor.hard.kill.after.millis";
  public static final String NUM_CORE_KILL_THREADS = "executor.num.core.kill.threads";

  public static final String NUM_CORE_THREAD_CHECK_THREADS = "executor.num.core.thread.check.threads";
  public static final String CHECK_THREADS_EVERY_MILLIS = "executor.check.threads.every.millis";

  public static final String MAX_TASK_MESSAGE_LENGTH = "executor.status.update.max.task.message.length";

  public static final String IDLE_EXECUTOR_SHUTDOWN_AFTER_MILLIS = "executor.idle.shutdown.after.millis";
  public static final String SHUTDOWN_STOP_DRIVER_AFTER_MILLIS = "executor.shutdown.stop.driver.after.millis";

  public static final String TASK_APP_DIRECTORY = "executor.task.app.directory";

  public static final String TASK_EXECUTOR_JAVA_LOG_PATH = "executor.task.java.log.path";
  public static final String TASK_EXECUTOR_BASH_LOG_PATH = "executor.task.bash.log.path";
  public static final String TASK_SERVICE_LOG_PATH = "executor.task.service.log.path";

  public static final String DEFAULT_USER = "executor.default.user";

  public static final String GLOBAL_TASK_DEFINITION_DIRECTORY = "executor.global.task.definition.directory";
  public static final String GLOBAL_TASK_DEFINITION_SUFFIX = "executor.global.task.definition.suffix";

  public static final String LOGROTATE_COMMAND = "executor.logrotate.command";
  public static final String LOGROTATE_CONFIG_DIRECTORY = "executor.logrotate.config.folder";
  public static final String LOGROTATE_STATE_FILE = "executor.logrotate.state.file";
  public static final String LOGROTATE_AFTER_BYTES = "executor.logrotate.after.bytes";  // TODO: figure this out
  public static final String LOGROTATE_DIRECTORY = "executor.logrotate.to.directory";
  public static final String LOGROTATE_MAXAGE_DAYS = "executor.logrotate.maxage.days";
  public static final String LOGROTATE_COUNT = "executor.logrotate.count";
  public static final String LOGROTATE_DATEFORMAT = "executor.logrotate.dateformat";

  public static final String LOGROTATE_EXTRAS_DATEFORMAT = "executor.logrotate.extras.dateformat";
  public static final String LOGROTATE_EXTRAS_FILES = "executor.logrotate.extras.files";

  public static final String TAIL_LOG_LINES_TO_SAVE = "executor.service.log.tail.lines.to.save";
  public static final String TAIL_LOG_FILENAME = "executor.service.log.tail.file.name";

  public static final String S3_FILES_TO_BACKUP = "executor.s3.uploader.extras.files";
  public static final String S3_UPLOADER_PATTERN = "executor.s3.uploader.pattern";
  public static final String S3_UPLOADER_BUCKET = "executor.s3.uploader.bucket";

  public static final String USE_LOCAL_DOWNLOAD_SERVICE = "executor.use.local.download.service";

  public static final String LOCAL_DOWNLOAD_SERVICE_TIMEOUT_MILLIS = "executor.local.download.service.timeout.millis";

  public static final String MAX_TASK_THREADS = "executor.max.task.threads";

  @NotEmpty
  private String executorJavaLog = "executor.java.log";

  @NotEmpty
  private String executorBashLog = "executor.bash.log";

  @NotEmpty
  private String serviceLog = "service.log";

  @NotEmpty
  private String defaultRunAsUser;

  @NotEmpty
  private String taskAppDirectory = "app";

  @Min(0)
  private long shutdownTimeoutWaitMillis = TimeUnit.MINUTES.toMillis(5);

  @Min(0)
  private long idleExecutorShutdownWaitMillis = TimeUnit.SECONDS.toMillis(30);

  @Min(0)
  private long stopDriverAfterMillis = TimeUnit.SECONDS.toMillis(5);

  @NotEmpty
  @DirectoryExists
  private String globalTaskDefinitionDirectory;

  @NotEmpty
  private String globalTaskDefinitionSuffix = ".task.json";

  @Min(1)
  private long hardKillAfterMillis = TimeUnit.MINUTES.toMillis(3);

  @Min(1)
  private int killThreads = 1;

  @Min(1)
  private int threadCheckThreads = 1;

  @Min(1)
  private long checkThreadsEveryMillis = TimeUnit.SECONDS.toMillis(5);

  @Min(0)
  private int maxTaskMessageLength = 80;

  @NotEmpty
  private String logrotateCommand = "logrotate";

  @NotEmpty
  private String logrotateStateFile = "logrotate.status";

  @NotEmpty
  @DirectoryExists
  private String logrotateConfDirectory = "/etc/logrotate.d";

  @NotEmpty
  private String logrotateToDirectory = "logs";

  @Min(1)
  private int logrotateMaxageDays = 7;

  @Min(1)
  private int logrotateCount = 20;

  @NotEmpty
  private String logrotateDateformat= "-%Y%m%d%s";

  @NotEmpty
  private String logrotateExtrasDateformat = "-%Y%m%d";

  @NotNull
  private List<String> logrotateExtrasFiles = Collections.emptyList();

  /**
   * Extra files to backup to S3 besides the service log.
   */
  @NotNull
  private List<String> additionalS3FilesToBackup = Collections.emptyList();

  @Min(1)
  private int tailLogLinesToSave = 500;

  @NotEmpty
  private String serviceFinishedTailLog = "tail_of_finished_service.log";

  @NotEmpty
  private String s3KeyPattern;

  @NotEmpty
  private String s3Bucket;

  private boolean useLocalDownloadService = false;

  @Min(1)
  private long localDownloadServiceTimeoutMillis = TimeUnit.MINUTES.toMillis(3);

  private Optional<Integer> maxTaskThreads = Optional.absent();

  public String getExecutorJavaLog() {
    return executorJavaLog;
  }

  public String getExecutorBashLog() {
    return executorBashLog;
  }

  public String getServiceLog() {
    return serviceLog;
  }

  public String getDefaultRunAsUser() {
    return defaultRunAsUser;
  }

  public String getTaskAppDirectory() {
    return taskAppDirectory;
  }

  public long getShutdownTimeoutWaitMillis() {
    return shutdownTimeoutWaitMillis;
  }

  public long getIdleExecutorShutdownWaitMillis() {
    return idleExecutorShutdownWaitMillis;
  }

  public long getStopDriverAfterMillis() {
    return stopDriverAfterMillis;
  }

  public String getGlobalTaskDefinitionDirectory() {
    return globalTaskDefinitionDirectory;
  }

  public String getGlobalTaskDefinitionSuffix() {
    return globalTaskDefinitionSuffix;
  }

  public long getHardKillAfterMillis() {
    return hardKillAfterMillis;
  }

  public int getKillThreads() {
    return killThreads;
  }

  public int getThreadCheckThreads() {
    return threadCheckThreads;
  }

  public long getCheckThreadsEveryMillis() {
    return checkThreadsEveryMillis;
  }

  public int getMaxTaskMessageLength() {
    return maxTaskMessageLength;
  }

  public String getLogrotateCommand() {
    return logrotateCommand;
  }

  public String getLogrotateStateFile() {
    return logrotateStateFile;
  }

  public Path getLogrotateConfDirectory() {
    return Paths.get(logrotateConfDirectory);
  }

  public String getLogrotateToDirectory() {
    return logrotateToDirectory;
  }

  public int getLogrotateMaxageDays() {
    return logrotateMaxageDays;
  }

  public int getLogrotateCount() {
    return logrotateCount;
  }

  public String getLogrotateDateformat() {
    return logrotateDateformat;
  }

  public String getLogrotateExtrasDateformat() {
    return logrotateExtrasDateformat;
  }

  public List<String> getLogrotateExtrasFiles() {
    return logrotateExtrasFiles;
  }

  public List<String> getAdditionalS3FilesToBackup() {
    return additionalS3FilesToBackup;
  }

  public int getTailLogLinesToSave() {
    return tailLogLinesToSave;
  }

  public String getServiceFinishedTailLog() {
    return serviceFinishedTailLog;
  }

  public String getS3KeyPattern() {
    return s3KeyPattern;
  }

  public String getS3Bucket() {
    return s3Bucket;
  }

  public boolean isUseLocalDownloadService() {
    return useLocalDownloadService;
  }

  public long getLocalDownloadServiceTimeoutMillis() {
    return localDownloadServiceTimeoutMillis;
  }

  public Optional<Integer> getMaxTaskThreads() {
    return maxTaskThreads;
  }

  public Path getTaskDefinitionPath(String taskId) {
    return Paths.get(getGlobalTaskDefinitionDirectory()).resolve(MesosUtils.getSafeTaskIdForDirectory(taskId) + getGlobalTaskDefinitionSuffix());
  }

  public void setExecutorJavaLog(String executorJavaLog) {
    this.executorJavaLog = executorJavaLog;
  }

  public void setExecutorBashLog(String executorBashLog) {
    this.executorBashLog = executorBashLog;
  }

  public void setServiceLog(String serviceLog) {
    this.serviceLog = serviceLog;
  }

  public void setDefaultRunAsUser(String defaultRunAsUser) {
    this.defaultRunAsUser = defaultRunAsUser;
  }

  public void setTaskAppDirectory(String taskAppDirectory) {
    this.taskAppDirectory = taskAppDirectory;
  }

  public void setShutdownTimeoutWaitMillis(long shutdownTimeoutWaitMillis) {
    this.shutdownTimeoutWaitMillis = shutdownTimeoutWaitMillis;
  }

  public void setIdleExecutorShutdownWaitMillis(long idleExecutorShutdownWaitMillis) {
    this.idleExecutorShutdownWaitMillis = idleExecutorShutdownWaitMillis;
  }

  public void setStopDriverAfterMillis(long stopDriverAfterMillis) {
    this.stopDriverAfterMillis = stopDriverAfterMillis;
  }

  public void setGlobalTaskDefinitionDirectory(String globalTaskDefinitionDirectory) {
    this.globalTaskDefinitionDirectory = globalTaskDefinitionDirectory;
  }

  public void setGlobalTaskDefinitionSuffix(String globalTaskDefinitionSuffix) {
    this.globalTaskDefinitionSuffix = globalTaskDefinitionSuffix;
  }

  public void setHardKillAfterMillis(long hardKillAfterMillis) {
    this.hardKillAfterMillis = hardKillAfterMillis;
  }

  public void setKillThreads(int killThreads) {
    this.killThreads = killThreads;
  }

  public void setThreadCheckThreads(int threadCheckThreads) {
    this.threadCheckThreads = threadCheckThreads;
  }

  public void setCheckThreadsEveryMillis(long checkThreadsEveryMillis) {
    this.checkThreadsEveryMillis = checkThreadsEveryMillis;
  }

  public void setMaxTaskMessageLength(int maxTaskMessageLength) {
    this.maxTaskMessageLength = maxTaskMessageLength;
  }

  public void setLogrotateCommand(String logrotateCommand) {
    this.logrotateCommand = logrotateCommand;
  }

  public void setLogrotateStateFile(String logrotateStateFile) {
    this.logrotateStateFile = logrotateStateFile;
  }

  public void setLogrotateConfDirectory(String logrotateConfDirectory) {
    this.logrotateConfDirectory = logrotateConfDirectory;
  }

  public void setLogrotateToDirectory(String logrotateToDirectory) {
    this.logrotateToDirectory = logrotateToDirectory;
  }

  public void setLogrotateMaxageDays(int logrotateMaxageDays) {
    this.logrotateMaxageDays = logrotateMaxageDays;
  }

  public void setLogrotateCount(int logrotateCount) {
    this.logrotateCount = logrotateCount;
  }

  public void setLogrotateDateformat(String logrotateDateformat) {
    this.logrotateDateformat = logrotateDateformat;
  }

  public void setLogrotateExtrasDateformat(String logrotateExtrasDateformat) {
    this.logrotateExtrasDateformat = logrotateExtrasDateformat;
  }

  public void setLogrotateExtrasFiles(List<String> logrotateExtrasFiles) {
    this.logrotateExtrasFiles = logrotateExtrasFiles;
  }

  public void setAdditionalS3FilesToBackup(List<String> additionalS3FilesToBackup) {
    this.additionalS3FilesToBackup = additionalS3FilesToBackup;
  }

  public void setTailLogLinesToSave(int tailLogLinesToSave) {
    this.tailLogLinesToSave = tailLogLinesToSave;
  }

  public void setServiceFinishedTailLog(String serviceFinishedTailLog) {
    this.serviceFinishedTailLog = serviceFinishedTailLog;
  }

  public void setS3KeyPattern(String s3KeyPattern) {
    this.s3KeyPattern = s3KeyPattern;
  }

  public void setS3Bucket(String s3Bucket) {
    this.s3Bucket = s3Bucket;
  }

  public void setUseLocalDownloadService(boolean useLocalDownloadService) {
    this.useLocalDownloadService = useLocalDownloadService;
  }

  public void setLocalDownloadServiceTimeoutMillis(long localDownloadServiceTimeoutMillis) {
    this.localDownloadServiceTimeoutMillis = localDownloadServiceTimeoutMillis;
  }

  public void setMaxTaskThreads(Optional<Integer> maxTaskThreads) {
    this.maxTaskThreads = maxTaskThreads;
  }

  @Override
  public String toString() {
    return "SingularityExecutorConfiguration [executorJavaLog=" + executorJavaLog + ", executorBashLog=" + executorBashLog + ", serviceLog=" + serviceLog + ", defaultRunAsUser=" + defaultRunAsUser
        + ", taskAppDirectory=" + taskAppDirectory + ", shutdownTimeoutWaitMillis=" + shutdownTimeoutWaitMillis + ", idleExecutorShutdownWaitMillis=" + idleExecutorShutdownWaitMillis
        + ", stopDriverAfterMillis=" + stopDriverAfterMillis + ", globalTaskDefinitionDirectory=" + globalTaskDefinitionDirectory + ", globalTaskDefinitionSuffix=" + globalTaskDefinitionSuffix
        + ", hardKillAfterMillis=" + hardKillAfterMillis + ", killThreads=" + killThreads + ", threadCheckThreads=" + threadCheckThreads + ", checkThreadsEveryMillis=" + checkThreadsEveryMillis
        + ", maxTaskMessageLength=" + maxTaskMessageLength + ", logrotateCommand=" + logrotateCommand + ", logrotateStateFile=" + logrotateStateFile + ", logrotateConfDirectory="
        + logrotateConfDirectory + ", logrotateToDirectory=" + logrotateToDirectory + ", logrotateMaxageDays=" + logrotateMaxageDays + ", logrotateCount=" + logrotateCount + ", logrotateDateformat="
        + logrotateDateformat + ", logrotateExtrasDateformat=" + logrotateExtrasDateformat + ", logrotateExtrasFiles=" + logrotateExtrasFiles + ", tailLogLinesToSave=" + tailLogLinesToSave + ", serviceFinishedTailLog=" + serviceFinishedTailLog
        + ", additionalS3FilesToBackup=" + additionalS3FilesToBackup + ", s3KeyPattern=" + s3KeyPattern + ", s3Bucket="
        + s3Bucket + ", useLocalDownloadService=" + useLocalDownloadService + ", localDownloadServiceTimeoutMillis=" + localDownloadServiceTimeoutMillis + ", maxTaskThreads=" + maxTaskThreads + "]";
  }

  @Override
  public void updateFromProperties(Properties properties) {
    final Splitter commaSplitter = Splitter.on(',').omitEmptyStrings().trimResults();

    if (properties.containsKey(SHUTDOWN_TIMEOUT_MILLIS)) {
      setShutdownTimeoutWaitMillis(Long.parseLong(properties.getProperty(SHUTDOWN_TIMEOUT_MILLIS)));
    }

    if (properties.containsKey(HARD_KILL_AFTER_MILLIS)) {
      setHardKillAfterMillis(Long.parseLong(properties.getProperty(HARD_KILL_AFTER_MILLIS)));
    }

    if (properties.containsKey(NUM_CORE_KILL_THREADS)) {
      setKillThreads(Integer.parseInt(properties.getProperty(NUM_CORE_KILL_THREADS)));
    }

    if (properties.containsKey(NUM_CORE_THREAD_CHECK_THREADS)) {
      setThreadCheckThreads(Integer.parseInt(properties.getProperty(NUM_CORE_THREAD_CHECK_THREADS)));
    }

    if (properties.containsKey(CHECK_THREADS_EVERY_MILLIS)) {
      setCheckThreadsEveryMillis(Long.parseLong(properties.getProperty(CHECK_THREADS_EVERY_MILLIS)));
    }

    if (properties.containsKey(MAX_TASK_MESSAGE_LENGTH)) {
      setMaxTaskMessageLength(Integer.parseInt(properties.getProperty(MAX_TASK_MESSAGE_LENGTH)));
    }

    if (properties.containsKey(IDLE_EXECUTOR_SHUTDOWN_AFTER_MILLIS)) {
      setIdleExecutorShutdownWaitMillis(Long.parseLong(properties.getProperty(IDLE_EXECUTOR_SHUTDOWN_AFTER_MILLIS)));
    }

    if (properties.containsKey(SHUTDOWN_STOP_DRIVER_AFTER_MILLIS)) {
      setShutdownTimeoutWaitMillis(Long.parseLong(properties.getProperty(SHUTDOWN_STOP_DRIVER_AFTER_MILLIS)));
    }

    if (properties.containsKey(TASK_APP_DIRECTORY)) {
      setTaskAppDirectory(properties.getProperty(TASK_APP_DIRECTORY));
    }

    if (properties.containsKey(TASK_EXECUTOR_JAVA_LOG_PATH)) {
      setExecutorJavaLog(properties.getProperty(TASK_EXECUTOR_JAVA_LOG_PATH));
    }

    if (properties.containsKey(TASK_EXECUTOR_BASH_LOG_PATH)) {
      setExecutorBashLog(properties.getProperty(TASK_EXECUTOR_BASH_LOG_PATH));
    }

    if (properties.containsKey(TASK_SERVICE_LOG_PATH)) {
      setServiceLog(properties.getProperty(TASK_SERVICE_LOG_PATH));
    }

    if (properties.containsKey(DEFAULT_USER)) {
      setDefaultRunAsUser(properties.getProperty(DEFAULT_USER));
    }

    if (properties.containsKey(GLOBAL_TASK_DEFINITION_DIRECTORY)) {
      setGlobalTaskDefinitionDirectory(properties.getProperty(GLOBAL_TASK_DEFINITION_DIRECTORY));
    }

    if (properties.containsKey(GLOBAL_TASK_DEFINITION_SUFFIX)) {
      setGlobalTaskDefinitionSuffix(properties.getProperty(GLOBAL_TASK_DEFINITION_SUFFIX));
    }

    if (properties.containsKey(LOGROTATE_COMMAND)) {
      setLogrotateCommand(properties.getProperty(LOGROTATE_COMMAND));
    }

    if (properties.containsKey(LOGROTATE_CONFIG_DIRECTORY)) {
      setLogrotateConfDirectory(properties.getProperty(LOGROTATE_CONFIG_DIRECTORY));
    }

    if (properties.containsKey(LOGROTATE_STATE_FILE)) {
      setLogrotateStateFile(properties.getProperty(LOGROTATE_STATE_FILE));
    }

    if (properties.containsKey(LOGROTATE_DIRECTORY)) {
      setLogrotateToDirectory(properties.getProperty(LOGROTATE_DIRECTORY));
    }

    if (properties.containsKey(LOGROTATE_MAXAGE_DAYS)) {
      setLogrotateMaxageDays(Integer.parseInt(properties.getProperty(LOGROTATE_MAXAGE_DAYS)));
    }

    if (properties.containsKey(LOGROTATE_COUNT)) {
      setLogrotateCount(Integer.parseInt(properties.getProperty(LOGROTATE_COUNT)));
    }

    if (properties.containsKey(LOGROTATE_DATEFORMAT)) {
      setLogrotateDateformat(properties.getProperty(LOGROTATE_DATEFORMAT));
    }

    if (properties.containsKey(LOGROTATE_EXTRAS_DATEFORMAT)) {
      setLogrotateExtrasDateformat(properties.getProperty(LOGROTATE_EXTRAS_DATEFORMAT));
    }

    if (properties.containsKey(LOGROTATE_EXTRAS_FILES)) {
      setLogrotateExtrasFiles(commaSplitter.splitToList(properties.getProperty(LOGROTATE_EXTRAS_FILES)));
    }

    if (properties.containsKey(TAIL_LOG_LINES_TO_SAVE)) {
      setTailLogLinesToSave(Integer.parseInt(properties.getProperty(TAIL_LOG_LINES_TO_SAVE)));
    }

    if (properties.containsKey(TAIL_LOG_FILENAME)) {
      setServiceFinishedTailLog(properties.getProperty(TAIL_LOG_FILENAME));
    }

    if (properties.containsKey(S3_FILES_TO_BACKUP)) {
      setAdditionalS3FilesToBackup(commaSplitter.splitToList(properties.getProperty(S3_FILES_TO_BACKUP)));
    }

    if (properties.containsKey(S3_UPLOADER_PATTERN)) {
      setS3KeyPattern(properties.getProperty(S3_UPLOADER_PATTERN));
    }

    if (properties.containsKey(S3_UPLOADER_BUCKET)) {
      setS3Bucket(properties.getProperty(S3_UPLOADER_BUCKET));
    }

    if (properties.containsKey(USE_LOCAL_DOWNLOAD_SERVICE)) {
      setUseLocalDownloadService(Boolean.parseBoolean(properties.getProperty(USE_LOCAL_DOWNLOAD_SERVICE)));
    }

    if (properties.containsKey(LOCAL_DOWNLOAD_SERVICE_TIMEOUT_MILLIS)) {
      setLocalDownloadServiceTimeoutMillis(Long.parseLong(properties.getProperty(LOCAL_DOWNLOAD_SERVICE_TIMEOUT_MILLIS)));
    }

    if (properties.containsKey(MAX_TASK_THREADS)) {
      setMaxTaskThreads(Optional.of(Integer.parseInt(properties.getProperty(MAX_TASK_THREADS))));
    }
  }
}
