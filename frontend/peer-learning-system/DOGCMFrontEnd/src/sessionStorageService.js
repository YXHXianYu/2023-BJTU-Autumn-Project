class SessionStorageService {
  static save(key, data) {
    sessionStorage.setItem(key, JSON.stringify(data));
  }

  static get(key) {
    const data = sessionStorage.getItem(key);
    return data ? JSON.parse(data) : null;
  }

  static remove(key) {
    sessionStorage.removeItem(key);
  }
}

export default SessionStorageService;