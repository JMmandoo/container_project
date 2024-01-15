// 지도 관련 변수 초기화
let mapContainer = document.getElementById('map');
let mapOption = {
  center: new kakao.maps.LatLng(37.56692, 126.97845),
  level: 3,
  mapTypeId: kakao.maps.MapTypeId.ROADMAP
};
let map = new kakao.maps.Map(mapContainer, mapOption);
let currentOverlay = null;

// 오버레이 관련 함수
function closeOverlay() {
  if (currentOverlay) {
    currentOverlay.setMap(null);
    currentOverlay = null;
  }
}

function attachOverlay(marker, content) {
  kakao.maps.event.addListener(marker, 'click', function () {
    closeOverlay();
    let overlay = new kakao.maps.CustomOverlay({
      content: content,
      map: map,
      position: marker.getPosition()
    });
    currentOverlay = overlay;
  });
}

// 주소 검색 기능
function searchAddress() {
  let addressInput = document.getElementById('addressInput').value;
  if (!addressInput) {
    alert('주소를 입력하세요.');
    return;
  }
  let geocoder = new kakao.maps.services.Geocoder();
  geocoder.addressSearch(addressInput, function (result, status) {
    if (status === kakao.maps.services.Status.OK && result.length > 0) {
      map.setCenter(new kakao.maps.LatLng(result[0].y, result[0].x));
    } else {
      alert('주소를 찾을 수 없습니다.');
    }
  });
}

// 지도 컨트롤 설정
function setupMapControls() {
  let mapTypeControl = new kakao.maps.MapTypeControl();
  map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

  let zoomControl = new kakao.maps.ZoomControl();
  map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
}

// 마커 이미지 설정
let imageSrc = '/assets/img/team/storage.png';
let imageSize = new kakao.maps.Size(40, 45);
let imageOption = {offset: new kakao.maps.Point(27, 69)};
let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

// 마커 클러스터러 설정
let clusterer = new kakao.maps.MarkerClusterer({
  map: map,
  averageCenter: true,
  minLevel: 5
});

// 데이터 로드 및 마커 생성
function loadData() {
  let xhr = new XMLHttpRequest();
  xhr.open("GET", "/conta", true);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      let data = JSON.parse(xhr.responseText).datas;
      data.forEach(function (item) {
        let marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(item.we, item.kyung),
          map: map,
          image: markerImage
        });

        let content = '<div class="wrap">' +
            '    <div class="info">' +
            '        <div class="title">' +
            '            창고 정보' +  // 제목을 변경
            '            <div class="close" onclick="closeOverlay()" title="닫기"></div>' +
            ' </div>' +
            ' <div class="body">' +
            ' <div class="img">' +
            ' <img src="/upload/' + item.cont_image + '" width="73" height="70">' + // 이미지를 추가
            ' </div>' +
            ' <div class="desc">' +
            ' <div style="display:none" class="ellipsis">창고번호: ' + item.con_no + '</div>' +
            ' <div class="ellipsis">회사명: ' + item.con_name + '</div>' +
            ' <div class="ellipsis">주소: ' + item.con_addr + '</div>' +
            ' <div class="ellipsis">크기: ' + item.con_area + ' </div>' +
            ' <div><a href="javascript:void(0);" onclick="openPopup(' + item.con_no + ')">창고 예약하기</a></div>' +
            ' </div>' +
            ' </div>' +
            '</div>';
        attachOverlay(marker, content);
        clusterer.addMarker(marker);
      });
    } else if (xhr.readyState === 4) {
      console.error("서버 응답 오류:", xhr.statusText);
    }
  };
  xhr.send();
}

// 창고 예약 팝업 열기
function openPopup(conNo) {
  let encodedConNo = encodeURIComponent(conNo);
  let url = 'booking/booking?con_no=' + encodedConNo;
  window.open(url, '_blank', 'width=1000, height=800');
}

// 교통 정보 표시 기능
let trafficShown = false;

function toggleTraffic() {
  if (trafficShown) {
    map.removeOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC);
    trafficShown = false;
  } else {
    map.addOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC);
    trafficShown = true;
  }
}

// 지도 컨트롤 및 데이터 로드 초기화
setupMapControls();
loadData();