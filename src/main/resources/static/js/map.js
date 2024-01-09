// 지도 초기화
  var mapContainer = document.getElementById('map');
  var mapOption = {
    center: new kakao.maps.LatLng(37.56692, 126.97845),
    level: 3,
    mapTypeId: kakao.maps.MapTypeId.ROADMAP
  };

  var currentOverlay = null;

// 오버레이 닫기 함수
  function closeOverlay() {
    if (currentOverlay) {
      currentOverlay.setMap(null);
    }
  }

// 오버레이 첨부 함수
  function attachOverlay(marker, content) {
    kakao.maps.event.addListener(marker, 'click', function () {
      closeOverlay();

      var overlay = new kakao.maps.CustomOverlay({
        content: content,
        map: map,
        position: marker.getPosition()
      });

      currentOverlay = overlay;
    });
  }

// 주소 검색 함수
  function searchAddress() {
    var addressInput = document.getElementById('addressInput');
    var address = addressInput.value;

    if (address) {
      var geocoder = new kakao.maps.services.Geocoder();

      geocoder.addressSearch(address, function (result, status) {
        if (status === kakao.maps.services.Status.OK && result.length > 0) {
          map.setCenter(new kakao.maps.LatLng(result[0].y, result[0].x));
        } else {
          alert('주소를 찾을 수 없습니다.');
        }
      });
    } else {
      alert('주소를 입력하세요.');
    }
  }

// 지도 생성
  var map = new kakao.maps.Map(mapContainer, mapOption);

// 기타 지도 관련 코드
  var imageSrc = '/assets/img/team/storage.png', // 마커이미지의 주소입니다
      imageSize = new kakao.maps.Size(40, 45), // 마커이미지의 크기입니다
      imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

// 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
  var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption),
      markerPosition = new kakao.maps.LatLng(37.54699, 127.09598); // 마커가 표시될 위치입니다

// 지도 타입 변경 컨트롤을 생성한다
  var mapTypeControl = new kakao.maps.MapTypeControl();

// 지도의 상단 우측에 지도 타입 변경 컨트롤을 추가한다
  map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

// 지도에 확대 축소 컨트롤을 생성한다
  var zoomControl = new kakao.maps.ZoomControl();

// 지도의 우측에 확대 축소 컨트롤을 추가한다
  map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
//********여기까지는 지도 컨트롤***********

// 지도에 교통정보를 표시하도록 지도타입을 추가합니다
  map.addOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC);

// 마커 클러스터러를 생성합니다
  var clusterer = new kakao.maps.MarkerClusterer({
    map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
    averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
    minLevel: 5 // 클러스터 할 최소 지도 레벨
  });

// 데이터 로드 및 마커 생성
  var xhr = new XMLHttpRequest();
  xhr.open("GET", "/conta", true);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      {
        var data = JSON.parse(xhr.responseText).datas;
        var markers = [];

        for (var i = 0; i < data.length; i++) {
          var marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(data[i].we, data[i].kyung), //좌표,좌표
            map: map,
            image: markerImage
          });

          markers.push(marker); // makers라는 변수안에 maker를 넣음

          // 마커를 클릭했을 때 커스텀 오버레이를 표시합니다
          var content = '<div class="wrap">' +
              '    <div class="info">' +
              '        <div class="title">' +
              '            창고 정보' +  // 제목을 변경
              '            <div class="close" onclick="closeOverlay()" title="닫기"></div>' +
              '        </div>' +
              '        <div class="body">' +
              '            <div class="img">' +
              '                <img src="/static/upload/' + data[i].con_image_url + '" width="73" height="70">' + // 이미지를 추가
              '           </div>' +
              '            <div class="desc">' +
              '                <div style="display:none" class="ellipsis">창고번호: ' + data[i].con_no + '</div>' +  // 데이터를 표시
              '                <div class="ellipsis">회사명: ' + data[i].con_name + '</div>' +  // 데이터를 표시
              '                <div class="ellipsis">주소: ' + data[i].con_addr + '</div>' +  // 데이터를 표시
              '                <div class="ellipsis">크기: ' + data[i].con_area + ' </div>' +  // 데이터를 표시
              '            <div><a href="javascript:void(0);" onclick="openPopup(' + data[i].con_no + ')">창고 예약하기</a></div>' +
              '            </div>' +
              '    </div>' +
              '</div>';

          // 마커를 클릭했을 때 커스텀 오버레이를 표시합니다
          attachOverlay(marker, content);
        }

        // 클러스터러에 마커들을 추가
        clusterer.addMarkers(markers);
      }
    } else {
      console.error("서버 응답 오류:", xhr.statusText);
    }
  };
  xhr.send();

// 커스텀 오버레이를 닫기 위해 호출되는 함수입니다
  function closeOverlay() {
    if (currentOverlay) {
      currentOverlay.setMap(null);
    }
  }

// 현재 표시 중인 오버레이를 저장하는 변수
  var currentOverlay = null;

// 마커를 클릭했을 때 커스텀 오버레이를 표시하는 함수
  function attachOverlay(marker, content) {
    kakao.maps.event.addListener(marker, 'click', function () {
      // 기존 오버레이가 있으면 닫기
      closeOverlay();

      var overlay = new kakao.maps.CustomOverlay({
        content: content,
        map: map,
        position: marker.getPosition()
      });

      // 클릭한 마커에 대한 오버레이를 현재 오버레이로 저장
      currentOverlay = overlay;

      // 창고 예약하기 링크를 눌렀을 때 팝업 열기
      var popupButton = overlay.getElement().querySelector('.popup-button');
      if (popupButton) {
        popupButton.addEventListener('click', function () {
          openPopup();
        });
      }
    });
  }

  function openPopup(conNo) {
    // conNo 값을 URL-인코딩합니다.
    var encodedConNo = encodeURIComponent(conNo);

    // 새 창을 열 때 데이터를 쿼리 매개변수로 전달합니다.
    var url = 'booking/booking?con_no=' + encodedConNo;

    // 새 창을 엽니다.
    window.open(url, '_blank', 'width=1000, height=800');
  }

  var trafficShown = false; // 초기에는 교통 정보가 표시되지 않음
// 페이지 로딩 시 교통 정보를 제거합니다
  map.removeOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC);

// 교통 정보를 추가 또는 제거하는 함수
  function toggleTraffic() {
    if (trafficShown) {
      // 교통 정보를 제거합니다
      map.removeOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC);
      trafficShown = false;
    } else {
      // 교통 정보를 추가합니다
      map.addOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC);
      trafficShown = true;
    }
  }
